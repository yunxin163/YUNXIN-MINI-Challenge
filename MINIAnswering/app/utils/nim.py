# -*- coding: utf-8 -*-
import requests
import time
import hashlib
import random
from flask import current_app


def get_nim_nonce():
    seed = "123456789abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ"
    sa = []
    for i in range(50):
        sa.append(random.choice(seed))
    return ''.join(sa)


def get_nim_headers():

    app_key = current_app.config['NIM_APPKEY']
    app_secret = current_app.config['NIM_SECRET']

    nonce = get_nim_nonce()
    cur_time = int(time.time())

    check_sum = hashlib.sha1()
    check_sum.update(('%s%s%s' % (app_secret, nonce, cur_time)).encode('utf-8'))

    return {
        'AppKey': app_key,
        'Nonce': nonce,
        'CurTime': str(cur_time),
        'CheckSum': check_sum.hexdigest(),
    }


# 新建 NIM Token
def create_nim_token(user, token):

    response = requests.post(
        'https://api.netease.im/nimserver/user/create.action',
        data={
            'accid': user.id,
            'name': user.username,
            'icon': user.avatar_uri,
            'token': token[-100:],
            'email': user.email,
            'mobile': user.telephone,
            'gender': user.gender,
        },
        headers=get_nim_headers(),
    )

    if response.json()['code'] == 200:
        return True
    else:
        return False


# 更新 NIM Token
def update_nim_token(user, token):

    response = requests.post(
        'https://api.netease.im/nimserver/user/update.action',
        data={
            'accid': user.id,
            'token': token[-100:],
        },
        headers=get_nim_headers(),
    )

    if response.json()['code'] == 200:
        return True
    else:
        return False


