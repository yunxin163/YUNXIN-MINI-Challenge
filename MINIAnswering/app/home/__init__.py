# -*- coding: utf-8 -*-
"""
Init api version1.0 Blueprint
"""
from flask import Blueprint

home = Blueprint('home', __name__)

from . import views

