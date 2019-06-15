# -*- coding: utf-8 -*-
"""
Init api version1.0 Blueprint
"""
from flask import Blueprint

users = Blueprint('users', __name__)

from . import views


