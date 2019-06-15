# -*- coding: utf-8 -*-
"""
Database Models for SQLAlchemy
"""
import time
from .. import db


class QuestionField(db.Model):

    __tablename__ = 'QuestionField'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True,
                   unique=True, index=True)
    question = db.Column(db.Integer)
    field = db.Column(db.Integer)

    def __init__(self, **kwargs):
        super(QuestionField, self).__init__(**kwargs)

    def to_dict(self):
        info_dict = {
            'id': self.id,
            'question': self.question,
            'field': self.field,
        }
        return info_dict

    def update_from_dict(self, dictt):
        for key, value in dictt.items():
            if hasattr(self, key):
                setattr(self, key, value)

