# -*- coding: utf-8 -*-
import hashlib
from flask import render_template, session, request, redirect, url_for, abort, send_from_directory, current_app
from . import users
from .. import db
from ..utils import *
from ..models import User
from json import dumps


def create_demo_user(username, nickname, password):

    # 对 password 进行 MD5 加密
    m2 = hashlib.md5()
    m2.update(password.encode('utf-8'))

    url = '%s/api/createDemoUser' % current_app.config['NIM_URL']
    headers = {
        'content-type': 'application/x-www-form-urlencoded',
        'appkey': current_app.config['NIM_APPKEY'],
        'Origin': 'https://app.yunxin.163.com',
        'Referer': 'https://app.yunxin.163.com/webdemo/education/',
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) App'
                      'leWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36',
    }
    data = {
        'username': username,
        'password': m2.hexdigest(),
        'nickname': nickname[0:9],
    }
    result = requests.post(url, data=data, headers=headers)

    print(result.json())

    return result.json()['res'] == 200


@users.route('/')
def home():

    try:
        token = request.cookies['token']
    except KeyError:
        return redirect(url_for('home.login'))

    user = User.verify_auth_token(token)

    # 判断用户合法性
    if user is None:
        return redirect(url_for('home.login'))

    return render_template('user/home.html',
                           title='迷你答疑 | 用户中心',
                           user=user,
                           token=token,
                           meta_data='<script>var user_information = %s;</script>' % dumps(user.to_dict()))


@users.route('/login/', methods=['POST'])
def login():

    try:
        username = request.form['username']
        password = request.form['password']
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    if is_valid_email(username):
        user = User.query.filter_by(email=username).first()
    else:
        user = User.query.filter_by(username=username).first()

    if username == '' or password == '':
        return response_json(states=1001, message='信息不完整')
    if user is None:
        return response_json(states=3001, message='用户不存在')
    elif user.verify_password(password) is False:
        return response_json(states=3002, message='密码输入错误')
    elif user.account_state == 0:
        return response_json(states=3003, message='请完成邮箱激活后登录')
    elif user.account_state != 1:
        return response_json(states=3004, message='账号被锁定')

    user.last_login_ip = request.remote_addr

    db.session.add(user)
    db.session.commit()

    token = user.generate_auth_token()

    data = user.to_dict()
    data['token'] = token

    data['nim_uid'] = user.username.lower()

    _md5 = hashlib.md5()
    _md5.update(user.username.lower().encode('utf-8'))

    data['nim_token'] = _md5.hexdigest()

    # 更新 NIM Token
    # if update_nim_token(user, token) is not True:
    #     if create_nim_token(user, token) is not True:
    #         return response_json(states=8002, message='无法向 NIM 新增 Token')

    return response_json(message='success', token=token, data=data)


@users.route('/logout/', methods=['POST'])
def logout():

    try:
        token = request.form['token']
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=1002, message='token 错误或已过期')

    current_app.token_blacklist.append(token)

    return response_json(message='success')


@users.route('/signup/', methods=['POST'])
def signup():

    try:
        username = request.form['username']
        email = request.form['email']
        password = request.form['password']
        confirm_password = request.form['confirmPassword']
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    if username == '' or email == '' or password == '' or confirm_password == '':
        return response_json(states=1001, message='信息不完整')
    if User.query.filter_by(email=email).first() is not None:
        return response_json(states=2001, message='邮箱已存在')
    if is_valid_email(email) is False:
        return response_json(states=2002, message='邮箱格式错误')
    if is_valid_password(password) is False:
        return response_json(states=2003, message='密码格式错误')
    if password != confirm_password:
        return response_json(states=2004, message='两次密码输入不相同')

    new_user = User(
        email=email,
        username=username,
        password=password,
    )

    db.session.add(new_user)
    db.session.commit()

    token = new_user.generate_auth_token()

    send_email(new_user.email, '确认您的账户',
               'auth/email/confirm', user=new_user, token=token)

    # 新建 NIM Token
    # if create_nim_token(new_user, token) is not True:
    #     return response_json(states=8002, message='无法向 NIM 新增 Token')

    if create_demo_user(username.lower(), username.lower(), password) is not True:
        return response_json(states=8003, message='无法向创建 Demo 账号')

    return response_json(message='激活邮件已发送')


@users.route('/confirm/', methods=['GET'])
def confirm():

    token = request.args['token']

    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=2005, message='验证链接已过期')

    if user.account_state != 0:
        return response_json(states=2006, message='用户已经激活')

    user.account_state = 1

    db.session.add(user)
    db.session.commit()

    return response_json(message='激活成功')


@users.route('/api/get_user_info/', methods=['POST'])
def api_user_info():
    try:
        token = request.form['token']
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=2001, message='token 已过期')

    return response_json(message='success', data=user.to_dict())


@users.route('/api/modify/', methods=['POST'])
def api_modify():

    try:
        token = request.cookies['token']
        username = request.form['username']
        email = request.form['email']
        gender = int(request.form['gender'])
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    # 判断用户合法性
    user = User.verify_auth_token(token)

    # 判断是否修改 email
    old_email = user.email
    if email != user.email:

        if User.query.filter_by(email=email).first() is not None:
            return response_json(states=2001, message='邮箱已存在')

        # 冻结用户
        user.account_state = 0

    if user is None:
        return response_json(states=1002, message='token 不正确或已过期')
    if username == '' or email == '' or gender == '':
        return response_json(states=1001, message='信息不完整')
    if is_valid_email(email) is False:
        return response_json(states=2002, message='邮箱格式错误')

    user.username = username
    user.email = email
    user.gender = gender

    db.session.add(user)
    db.session.commit()

    # 发送确认邮件
    if old_email != user.email:
        new_token = user.generate_auth_token()
        send_email(user.email, '验证您的邮箱',
                   'auth/email/change_email',
                   user=user, token=new_token)

    return response_json(message='success')


@users.route('/search/', methods=['POST'])
def search():
    try:
        query = request.form['query']
        token = request.cookies['token']

    except KeyError:
        return response_json(states=1001, message='信息不完整')

    # 判断用户合法性
    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=1002, message='token 不正确或已过期')

    # 用户搜索
    all_users = User.query.filter((User.username.contains(query))).order_by(User.reg_time.desc()).all()

    user_data = []
    for this_user in all_users:
        user_data.append(safe_user_dict(this_user))

    return response_json(message='success', data=user_data)



