# -*- coding: utf-8 -*-
import os

from flask import render_template, session, request, redirect, url_for, abort, send_from_directory
from . import home
from .. import db
from json import dumps


@home.route('/')
def index():
    return render_template('index.html',
                           title='迷你答疑 | 云答疑平台')


@home.route('/login/')
def login():
    return render_template('login.html',
                           title='迷你答疑 | 登录')


@home.route('/signup/')
def signup():
    return render_template('signup.html',
                           title='迷你答疑 | 注册')
