import { Entity, Column, ManyToMany, JoinTable } from 'typeorm';
import { BaseEntity } from '../utils/BaseEntity';

@Entity()
export class User extends BaseEntity {
  // 用户登录名
  @Column({
    unique: true
  })
  username: string;

  // 用户手机号
  @Column({
    unique: true
  })
  phone_number: string;  

  // 用户登录密码
  @Column({
    length: 32,
    select: false
  })
  password: string;

  // 用户昵称
  @Column({
    default: "次元信"
  })
  nickname: string;

  // 用户签名
  @Column({
    nullable: true,
    default: "暂无个人签名"
  })
  signature: string;

  // 用户头像
  @Column({
    default: "default.png"
  })
  avatar: string;  

  // 好友列表
  @ManyToMany(type => User)
  @JoinTable()
  friends: User[];  

  // 网易 token
  @Column({ nullable: true })
  token: string;
}

@Entity()
export class SMSCode extends BaseEntity {
  // 用户手机号
  @Column({
    unique: true
  })
  phone_number: string;

  // 验证码
  @Column()
  sms_code: string;

  // 是否已验证
  @Column({
    default: false
  })
  used: boolean;
}