# -*- coding: utf-8 -*-
"""
Database Models for SQLAlchemy
"""
from .. import db


class Field(db.Model):

    __tablename__ = 'Field'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True,
                   unique=True, index=True)
    name = db.Column(db.String(255))
    pic = db.Column(db.String(255))
    description = db.Column(db.String(255))

    def __init__(self, **kwargs):
        super(Field, self).__init__(**kwargs)

    def to_dict(self):
        info_dict = {
            'id': self.id,
            'name': self.name,
            'pic': self.pic,
            'description': self.description,
        }
        return info_dict

    def update_from_dict(self, dictt):
        for key, value in dictt.items():
            if hasattr(self, key):
                setattr(self, key, value)

