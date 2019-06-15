# -*- coding: utf-8 -*-
"""
Database Models for SQLAlchemy
"""
import time
from datetime import datetime
from .. import db


class Answer(db.Model):

    __tablename__ = 'Answer'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True,
                   unique=True, index=True)
    question = db.Column(db.Integer)
    respondent = db.Column(db.Integer)
    respond_time = db.Column(db.DateTime(), default=datetime.utcnow)

    def __init__(self, **kwargs):
        super(Answer, self).__init__(**kwargs)

    def to_dict(self):
        info_dict = {
            'id': self.id,
            'question': self.question,
            'respondent': self.respondent,
            'respond_time': time.mktime(self.respond_time.timetuple()) * 1000,
        }
        return info_dict

    def update_from_dict(self, dictt):
        for key, value in dictt.items():
            if hasattr(self, key):
                setattr(self, key, value)

