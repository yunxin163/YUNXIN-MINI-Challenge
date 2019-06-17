import * as md5 from 'md5';
import * as jwt from 'jsonwebtoken';
import { PRIVATE_KEY } from '../auth';
import { BaseContext } from 'koa';
import { User, SMSCode } from '../entity'
import { getManager } from 'typeorm';
import { Validator } from 'class-validator';
import axios from 'axios';
import * as fs from 'fs';
import * as path from 'path';
import sha1 = require('sha1');

const validator = new Validator();

export default class UserController {
  static async login(ctx: BaseContext) {
    const { username, password } = ctx.request.body;
    const userRepository = getManager().getRepository(User);

    if (validator.isEmpty(username)) {
      return ctx.error({ msg: '用户名不得为空' });
    }
    if (validator.isEmpty(password)) {
      return ctx.error({ msg: '密码不得为空' });
    }

    const user = await userRepository.findOne({ username, is_delete: false }, {select: ['id', 'password']});
    if (!user) {
      return ctx.error({ msg: '用户未注册' });
    }

    const md5Paaword = md5(password);
    if (validator.equals(user.password, md5Paaword)) {
      // 验证成功，给用户分配 jwt 签名
      const payload = { id: user.id };
      const token = jwt.sign(payload, PRIVATE_KEY);
      ctx.success({ data: { token } });
    } else {
      ctx.error({ msg: '账号密码不匹配，无法登录！' });
    }
  }

  static async logout(ctx: BaseContext) {
    ctx.success({ msg: '登出成功' });
  }

  static async info(ctx: BaseContext) {
    if (ctx.state.user) {
      const user: User = ctx.state.user;
      ctx.success({ data: user })
    }
  }

  static async addUser(ctx: BaseContext) {
    const { phone_number, password } = ctx.request.body;
    const userRepository = getManager().getRepository(User);
    const userToSaved = new User();

    if (!validator.isPhoneNumber(phone_number, 'CN')) {
      return ctx.error({ msg: '手机号不合法' });
    }
    if (validator.isEmpty(password) || (password.length > 32 || password.length < 8)) {
      return ctx.error({ msg: '密码长度不合法' });
    }
    if (await userRepository.findOne({ username: phone_number })) {
      return ctx.error({ msg: '该手机号已注册' });
    }
    // 验证码验证
    const SMSRepository = getManager().getRepository(SMSCode);
    const smsCode = await SMSRepository.findOne({ phone_number })
    if (!smsCode || smsCode.used === false) {
      return ctx.error({ msg: '请先通过验证码验证' });
    }
    userToSaved.phone_number = userToSaved.username = phone_number;
    userToSaved.password = md5(password);
    await SMSRepository.remove(smsCode);
    await userRepository.save(userToSaved);
    ctx.success({ msg: '账号注册成功', data: await userRepository.findOne({ username: phone_number }) });
  }

  static async editUser(ctx: BaseContext) {
    const { nickname, signature } = ctx.request.body;
    if (ctx.state.user) {
      const user: User = ctx.state.user;
      const userRepository = getManager().getRepository(User);
      if (!validator.isEmpty(nickname)) {
        user.nickname = nickname;
      }
      if (!validator.isEmpty(signature)) {
        user.signature = signature;
      }
      userRepository.save(user);
      ctx.success({ msg: '修改用户信息成功' });
    }
  }

  static async friends(ctx: BaseContext) {
    const userRepository = getManager().getRepository(User);
    const user: User = await userRepository.findOne({
      where: { id: ctx.state.user.id },
      relations: ['friends']
    });
    if (user.friends) {
      return ctx.success({ data: user.friends });
    }
    ctx.error({ msg: '获取好友列表失败' });
  }

  static async addFriend(ctx: BaseContext) {
    const { phone_number } = ctx.request.body;
    const userRepository = getManager().getRepository(User);
    const friend = await userRepository.findOne({
      where: { phone_number },
      relations: ['friends']
    });
    if (!friend) {
      return ctx.error({ msg: '找不到该用户' });
    }
    if (ctx.state.user.username === friend.username) {
      return ctx.error({ msg: '不能添加自己为好友' });
    }
    const user: User = await userRepository.findOne({
      where: { id: ctx.state.user.id },
      relations: ['friends']
    });
    if (user.friends.find(friend => friend.phone_number === phone_number) !== undefined) {
      return ctx.error({ msg: '你已添加该用户为好友，请勿重复添加' });
    }
    user.friends.push(friend);
    friend.friends.push(user);
    await userRepository.save(user);
    await userRepository.save(friend);
    ctx.success({ msg: '添加好友成功', data: friend });
  }

  static async uploadAvatar(ctx: BaseContext) {
    const userRepository = getManager().getRepository(User);
    const user: User = ctx.state.user;

    if (ctx.req.file) {
      // 先删除原来的头像
      if (user.avatar !== "" && user.avatar !== "default.png") {
        const filename = path.join(__dirname, '../../uploads/avatar/', user.avatar)
        if (fs.existsSync(filename)) {
          fs.unlinkSync(filename);
        }
      }
      user.avatar = ctx.req.file.filename;
      await userRepository.save(user);

      ctx.success({ msg: '头像上传成功', data: { 
        avatar: ctx.req.file.filename
      }});
    } else {
      ctx.error({ msg: '头像上传失败' });
    }
  }

  static async getSMScode(ctx: BaseContext) {
    const { phone_number } = ctx.request.body;

    // 查询是否已经发送过验证码
    const SMSRepository = getManager().getRepository(SMSCode);
    const smsCode = await SMSRepository.findOne({ phone_number })
    if (smsCode) {
      const delta = new Date().valueOf() - smsCode.create_at.valueOf();
      if (delta <= 60 * 1000) {
        return ctx.error({ msg: '请勿频繁获取验证码' });
      } else {
        await SMSRepository.remove(smsCode);
      }
    }

    const newSMSCode = new SMSCode();
    newSMSCode.phone_number = phone_number;
    newSMSCode.sms_code = (10000 + Math.ceil(Math.random()*90000)).toString();

    const url = `http://yzxyzm.market.alicloudapi.com/yzx/verifySms?phone=${phone_number}&templateId=TP18040314&variable=code:${newSMSCode.sms_code}`
    const res = await axios.post(url, { }, {
      headers: {
        Authorization: 'APPCODE 7a4790b3e46d42919eeec506d98caf07'
      }
    });

    if (res.data.return_code === "00000") {
      await SMSRepository.save(newSMSCode);
      return ctx.success({ msg: '验证码已发送' });
    } else {
      return ctx.error({ msg: '短信服务器错误，验证码失败' });
    }
  }

  static async validateSMScode(ctx: BaseContext) {
    const { phone_number, sms_code } = ctx.request.body;
    console.log(ctx.request.body);
    if (validator.isEmpty(sms_code) || validator.isEmpty(phone_number)) {
      return ctx.error({ msg: '请求参数错误' });
    }
    const SMSRepository = getManager().getRepository(SMSCode);
    const smsCode = await SMSRepository.findOne({ phone_number })
    if (!smsCode) {
      return ctx.error({ msg: '请先获取验证码' });
    }
    if (validator.equals(smsCode.sms_code, sms_code)) {
      smsCode.used = true;
      await SMSRepository.save(smsCode);
      return ctx.success({ msg: '验证码验证成功' });
    } else {
      return ctx.error({ msg: '验证码错误' });
    }
  }

  static async getNeteaseToken(ctx: BaseContext) {
    if (ctx.state.user) {
      const userRepository = getManager().getRepository(User);
      const user: User = await userRepository.findOne({ id: ctx.state.user.id });

      if (!validator.isEmpty(user.token)) {
        return ctx.success({ data: {
          token: user.token,
          account: user.phone_number
        }});
      }

      const appKey = '45c6af3c98409b18a84451215d0bdd6e';
      const appSecret = '37db6b60';
      const nonce = Math.ceil(Math.random()*100000).toString();
      const curTime = Math.round(new Date().getTime() / 1000).toString();
      const checkSum = sha1(appSecret + nonce + curTime);

      const res = await axios.post(`https://api.netease.im/nimserver/user/create.action?accid=${user.phone_number}`, { }, {
        headers: {
          AppKey: appKey,
          Nonce: nonce,
          CurTime: curTime,
          CheckSum: checkSum,
          'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
        }
      });
      
      console.log(res.data)
      if (res.data.code == 200) {
        const token = res.data.info.token;
        user.token = token;
        await userRepository.save(user);
        ctx.success({ data: {
          token
        }});
      } else {
        ctx.error({ msg: '获取token失败' });
      }
    } else {
      ctx.error({ msg: '请先登录' });
    }
  }

  // public static getCheckSum(appSecret: string, nonce: string, curTime: string) {
  //   return sha1(appSecret + nonce + curTime);
  // }
}