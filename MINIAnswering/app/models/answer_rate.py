# -*- coding: utf-8 -*-
"""
Database Models for SQLAlchemy
"""

from .. import db


class AnswerRate(db.Model):

    __tablename__ = 'AnswerRate'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True,
                   unique=True, index=True)
    answer = db.Column(db.Integer)
    rate = db.Column(db.Integer)

    def __init__(self, **kwargs):
        super(AnswerRate, self).__init__(**kwargs)

    def to_dict(self):
        info_dict = {
            'id': self.id,
            'answer': self.answer,
            'rate': self.rate,
        }
        return info_dict

    def update_from_dict(self, dictt):
        for key, value in dictt.items():
            if hasattr(self, key):
                setattr(self, key, value)

