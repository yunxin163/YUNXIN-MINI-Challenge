# -*- coding: utf-8 -*-
from config import config
from flask_sqlalchemy import SQLAlchemy
from flask import Flask, render_template, current_app
from flask_msearch import Search
from flask_mail import Mail

db = SQLAlchemy()
search = Search(db=db)
mail = Mail()


def render_error_page(error_code):

    error_reasons = {
        400: u'请求错误。',
        404: u'您请求的资源不存在。',
        401: u'未经授权的错误。',
        403: u'禁止访问。',
        408: u'请求超时。',
        409: u'请求太多。',
        500: u'内部服务器错误。',
        502: u'错误的网关。',
        503: u'服务不可用。',
        504: u'网关超时。'
    }

    if error_code in error_reasons.keys():
        error_reason = error_reasons[error_code]
    else:
        error_reason = u'未知错误。'

    return render_template(
        'error.html',
        title=u'MINI 答疑 | %s' % error_code,
        keywords=current_app.config["WEBSITE_KEYWORDS"],
        description=current_app.config["WEBSITE_DESCRIPTION"],
        error_code=error_code,
        error_reason=error_reason,
    ), error_code


def create_app(config_name):

    app = Flask(__name__)
    app.config.from_object(config[config_name])
    config[config_name].init_app(app)

    db.init_app(app)
    search.init_app(app)
    mail.init_app(app)

    from .admin import admin as admin_blueprint
    app.register_blueprint(admin_blueprint, url_prefix='/admin')

    from .answer import answer as answer_blueprint
    app.register_blueprint(answer_blueprint, url_prefix='/answer')

    from .home import home as home_blueprint
    app.register_blueprint(home_blueprint, url_prefix='/')

    from .inbox import inbox as inbox_blueprint
    app.register_blueprint(inbox_blueprint, url_prefix='/inbox')

    from .question import question as question_blueprint
    app.register_blueprint(question_blueprint, url_prefix='/question')

    from .users import users as users_blueprint
    app.register_blueprint(users_blueprint, url_prefix='/user')

    @app.errorhandler(404)
    def app_error(error):
        error_code = error.code
        return render_error_page(error_code)

    return app

