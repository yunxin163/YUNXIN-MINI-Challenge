# -*- coding: utf-8 -*-
"""
Database Models for SQLAlchemy
"""
import time
from datetime import datetime
from .. import db


class Point(db.Model):

    __tablename__ = 'Point'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True,
                   unique=True, index=True)
    from_user = db.Column(db.Integer)
    to_user = db.Column(db.Integer)
    amount = db.Column(db.Integer)
    transfer_time = db.Column(db.DateTime(), default=datetime.utcnow)

    def __init__(self, **kwargs):
        super(Point, self).__init__(**kwargs)

    def to_dict(self):
        info_dict = {
            'id': self.id,
            'from_user': self.from_user,
            'to_user': self.to_user,
            'amount': self.amount,
            'transfer_time': time.mktime(self.transfer_time.timetuple()) * 1000,
        }
        return info_dict

    def update_from_dict(self, dictt):
        for key, value in dictt.items():
            if hasattr(self, key):
                setattr(self, key, value)

