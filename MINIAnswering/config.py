# -*- coding: utf-8 -*-
import os


class Config:
    """
    Basic and common config
    """
    TRAP_HTTP_EXCEPTIONS = True

    SECRET_KEY = os.environ.get('SECRET_KEY')

    # 邮件相关
    # TODO: 更新邮件地址
    MAIL_SERVER = os.environ.get('MAIL_SERVER')
    MAIL_PORT = int(os.environ.get('MAIL_PORT'))
    MAIL_USE_TLS = os.environ.get('MAIL_USE_TLS').lower() in \
                   ['true', 'on', '1']
    MAIL_USERNAME = os.environ.get('MAIL_USERNAME')
    MAIL_PASSWORD = os.environ.get('MAIL_PASSWORD')
    MAIL_SUBJECT_PREFIX = '[Mini 答疑]'
    MAIL_SENDER = 'onepass@tan90.co'

    # TODO: 更新数据库地址
    SQLALCHEMY_DATABASE_URI = os.environ.get('SQLALCHEMY_DATABASE_URI')
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    SQLALCHEMY_RECORD_QUERIES = True
    SLOW_DB_QUERY_TIME = 0.5
    SQLALCHEMY_POOL_RECYCLE = 27000

    # 网易云信 NIM
    NIM_APPKEY = '3bd40ad8bfaf925815be99a7479adace'
    NIM_SECRET = 'a664ed1c220b'

    WEBSITE_KEYWORDS = u'MINI答疑,MINI,答疑,Mini Answering'
    WEBSITE_DESCRIPTION = u'云在线答疑平台'

    @staticmethod
    def init_app(app):
        pass


class DevelopmentConfig(Config):
    """
    Development environment
    """
    DEBUG = True
    FLASK_DEBUG = 1


class TestingConfig(Config):
    """
    Testing environment
    """
    TESTING = True
    WTF_CSRF_ENABLED = False


class ProductionConfig(Config):
    """
    Production environment
    """
    DEBUG = False


config = {
    'development': DevelopmentConfig,
    'testing': TestingConfig,
    'production': ProductionConfig,

    'default': DevelopmentConfig
}