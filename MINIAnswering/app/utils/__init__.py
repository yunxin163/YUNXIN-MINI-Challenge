import random

from .response import *
from .validation import *
from .email import *
from .nim import *


def random_str(length=4):
    seed = "123456789abcdefghijklmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ"
    sa = []
    for i in range(length):
        sa.append(random.choice(seed))
    return ''.join(sa)


def safe_user_dict(user):

    if user is None:
        return {}

    user_dict = user.to_dict()

    user_dict.pop('telephone')
    user_dict.pop('account_state')
    user_dict.pop('reg_time')
    user_dict.pop('last_login_time')
    user_dict.pop('last_login_ip')

    return user_dict


