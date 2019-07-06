# -*- coding: utf-8 -*-
"""
Database Models for SQLAlchemy
"""
import time
import re
from datetime import datetime
from .. import db
from .answer import Answer
from .user import User
from .follow_question import FollowQuestion
from .question_field import QuestionField
from .field import Field
from ..utils import safe_user_dict


TAG_RE = re.compile(r'<[^>]+>')


class Question(db.Model):

    __tablename__ = 'Question'

    id = db.Column(db.Integer, primary_key=True, autoincrement=True,
                   unique=True, index=True)
    title = db.Column(db.String(255))
    content = db.Column(db.TEXT)
    publisher = db.Column(db.Integer)
    publish_time = db.Column(db.DateTime(), default=datetime.utcnow)
    viewed = db.Column(db.Integer, default=0)
    chatroom_id = db.Column(db.String(25))

    def __init__(self, **kwargs):
        super(Question, self).__init__(**kwargs)

    def to_dict(self):
        # 判断问题是否已经回答
        is_question_solve = True
        answer = Answer.query.filter_by(question=self.id).first()
        if answer is None:
            is_question_solve = False

        # 取问题关注数
        follow_question = FollowQuestion.query.filter_by(question=self.id).all()
        followed_by = len(follow_question)

        # 取提问者信息
        publisher_dict = safe_user_dict(User.query.filter_by(id=self.publisher).first())

        # 取问题领域
        fields = []
        question_fields = QuestionField.query.filter_by(question=self.id).all()
        for question_field in question_fields:
            field_id = question_field.field
            field = Field.query.filter_by(id=field_id).first()
            fields.append({
                'id': field_id,
                'name': field.name,
            })

        content_text = TAG_RE.sub('', self.content)

        info_dict = {
            'id': self.id,
            'title': self.title,
            'content': self.content,
            'content_summary': (content_text[:250] + '...') if len(content_text) > 250 else content_text,
            'publish_time': time.mktime(self.publish_time.timetuple()) * 1000,
            'viewed': self.viewed,
            'followed_by': followed_by,
            'is_question_solve': is_question_solve,
            'publisher': publisher_dict,
            'fields': fields,
            'chatroom_id': self.chatroom_id,
        }
        return info_dict

    def update_from_dict(self, dictt):
        for key, value in dictt.items():
            if hasattr(self, key):
                setattr(self, key, value)

