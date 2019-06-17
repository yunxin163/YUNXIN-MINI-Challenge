# -*- coding: utf-8 -*-
"""
Database Models for SQLAlchemy
"""
import time
from datetime import datetime
from .. import db


class FollowQuestion(db.Model):

    __tablename__ = 'FollowQuestion'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True,
                   unique=True, index=True)
    question = db.Column(db.Integer)
    user = db.Column(db.Integer)
    follow_time = db.Column(db.DateTime(), default=datetime.utcnow)

    def __init__(self, **kwargs):
        super(FollowQuestion, self).__init__(**kwargs)

    def to_dict(self):
        info_dict = {
            'id': self.id,
            'question': self.question,
            'user': self.user,
            'follow_time': time.mktime(self.follow_time.timetuple()) * 1000,
        }
        return info_dict

    def update_from_dict(self, dictt):
        for key, value in dictt.items():
            if hasattr(self, key):
                setattr(self, key, value)

