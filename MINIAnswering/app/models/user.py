# -*- coding: utf-8 -*-
"""
Database Models for SQLAlchemy
"""
import time
from datetime import datetime
import hashlib
from werkzeug.security import generate_password_hash, check_password_hash
from itsdangerous import TimedJSONWebSignatureSerializer as Serializer
from flask import current_app, request, url_for
from flask_login import UserMixin
from .. import db


class User(UserMixin, db.Model):

    __tablename__ = 'User'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True, unique=True)
    email = db.Column(db.String(40), unique=True, index=True)
    telephone = db.Column(db.String(20))
    username = db.Column(db.String(20), unique=True)
    gender = db.Column(db.Integer, default=2)
    password_hash = db.Column(db.String(255))
    account_state = db.Column(db.Integer, default=0)
    avatar_uri = db.Column(db.String(255), default='')
    reg_time = db.Column(db.DateTime(), default=datetime.utcnow)

    last_login_time = db.Column(db.DateTime(), default=datetime.utcnow)
    last_login_ip = db.Column(db.String(255))

    def __init__(self, **kwargs):
        super(User, self).__init__(**kwargs)
        if self.email is not None and self.avatar_uri is None:
            self.avatar_uri = self.gravatar()  # 自动使用 gravatar 作为头像
        self.ping()

    def ping(self):
        self.last_login_time = datetime.utcnow()
        db.session.add(self)

    @property
    def password(self):
        raise AttributeError('password is not a readable attribute')

    @password.setter
    def password(self, password):
        self.password_hash = generate_password_hash(password)

    def verify_password(self, password):
        return check_password_hash(self.password_hash, password)

    def gravatar_hash(self):
        return hashlib.md5(self.email.lower().encode('utf-8')).hexdigest()

    def gravatar(self, size=100, default='identicon', rating='g'):
        url = 'https://secure.gravatar.com/avatar'
        hash = self.gravatar_hash()
        return '{url}/{hash}?s={size}&d={default}&r={rating}'.format(
            url=url, hash=hash, size=size, default=default, rating=rating)

    def to_dict(self):
        json_user = {
            'id': self.id,
            'email': self.email,
            'telephone': self.telephone,
            'username': self.username,
            'gender': self.gender,
            'account_state': self.account_state,
            'avatar_uri': self.avatar_uri,
            'reg_time': time.mktime(self.reg_time.timetuple()) * 1000,
            'last_login_time': time.mktime(self.last_login_time.timetuple()) * 1000,
            'last_login_ip': self.last_login_ip,
        }
        return json_user

    @staticmethod
    def _update_token_blacklist():
        last = current_app.token_blacklist_lastappend
        now = datetime.now()
        if (now - last).seconds > 3600 and \
                len(current_app.token_blacklist) > 0:
            current_app.token_blacklist.pop(0)
        current_app.token_blacklist_lastappend = now

    @staticmethod
    def _is_black_token(token):
        result = False
        if token in current_app.token_blacklist:
            result = True
        User._update_token_blacklist()
        return result

    @staticmethod
    def _init_token_attr():
        if not hasattr(current_app, 'token_blacklist'):
            current_app.token_blacklist = list()
        if not hasattr(current_app, 'token_blacklist_lastappend'):
            current_app.token_blacklist_lastappend = datetime.now()

    def generate_auth_token(self, expiration=86400):
        User._init_token_attr()
        User._update_token_blacklist()

        s = Serializer(current_app.config['SECRET_KEY'],
                       expires_in=expiration)
        return s.dumps({'id': self.id}).decode('utf-8')

    @staticmethod
    def verify_auth_token(token):
        """
        return None if token is not valid else return User.id
        """
        User._init_token_attr()
        if User._is_black_token(token):
            return None
        s = Serializer(current_app.config['SECRET_KEY'])
        try:
            data = s.loads(token)
        except:
            return None
        return User.query.get(data['id'])

    def __repr__(self):
        return '<User %r>' % self.username

    def update_from_dict(self, dictt):
        for key, value in dictt.items():
            if hasattr(self, key):
                setattr(self, key, value)

