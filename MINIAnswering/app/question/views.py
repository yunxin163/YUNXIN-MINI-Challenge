# -*- coding: utf-8 -*-
import json
from flask import render_template, session, request, redirect, url_for, abort, send_from_directory
from . import question
from .. import db
from json import dumps
from ..utils import *
from ..models import User, Field, Question, QuestionField, FollowQuestion, Answer
from sqlalchemy.sql import or_, and_, func, desc


# 获取 chatroom 房间号
def chatroom_create(creator, name):

    url = '%s/api/chatroom/create' % current_app.config['NIM_URL']
    headers = {
        'content-type': 'application/json;charset=UTF-8',
        'appkey': current_app.config['NIM_APPKEY'],
        'Origin': 'https://app.yunxin.163.com',
        'Referer': 'https://app.yunxin.163.com/webdemo/education/',
        'sec-ch-ua': 'Google Chrome 75',
        'Sec-Fetch-Dest': 'empty',
        'Sec-Fetch-Mode': 'cors',
        'Sec-Fetch-Site': 'cross-site',
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) App'
                      'leWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36',
    }
    payload_data = {
        'creator': creator,
        'name': name,
    }
    result = requests.post(url, data=json.dumps(payload_data), headers=headers)

    return result.json()


@question.route('/create_chatroom/', methods=['POST'])
def create_chatroom():
    try:
        question_id = request.form['question_id']
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    question = Question.query.filter_by(id=question_id).first()

    if question is None:
        return response_json(states=1002, message='问题不存在')

    publisher = User.query.filter_by(id=question.publisher).first()

    chatroom_data = chatroom_create(publisher.username.lower(), 'MA_CR_{}'.format(question.id))

    question.chatroom_id = chatroom_data['msg']

    db.session.add(question)
    db.session.commit()

    return response_json(message='success', data=chatroom_data)


@question.route('/ask/')
def ask():

    # 生成 fields 数据
    fields_data = []
    fields = Field.query.all()
    for field in fields:
        fields_data.append(field.to_dict())

    return render_template('question/ask.html',
                           title='迷你答疑 | 提问',
                           fields_data='<script>var fields_data = %s;</script>' % dumps(fields_data))


# 获取 chatroom 服务器地址 (unused)
@question.route('/chatroom/getAddress/', methods=['POST'])
def chatroom_get_address():

    try:
        uid = request.form['uid']
        roomid = request.form['roomid']
    except KeyError:
        return {'res': -1}

    url = '%s/api/chatroom/getAddress' % current_app.config['NIM_URL']
    headers = {
        'content-type': 'application/x-www-form-urlencoded',
        'appkey': current_app.config['NIM_APPKEY'],
        'Origin': 'https://app.yunxin.163.com',
        'Referer': 'https://app.yunxin.163.com/webdemo/education/',
        'sec-ch-ua': 'Google Chrome 75',
        'Sec-Fetch-Dest': 'empty',
        'Sec-Fetch-Mode': 'cors',
        'Sec-Fetch-Site': 'cross-site',
        'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) App'
                      'leWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36',
    }
    data = {
        'uid': uid,
        'roomid': roomid,
    }
    result = requests.post(url, data=data, headers=headers)

    return result.text


@question.route('/ask/action/', methods=['POST'])
def ask_action():

    try:
        token = request.cookies['token']
        title = request.form['title']
        fields = request.form['fields']
        content = request.form['content']
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=1002, message='token 不正确或已过期')

    if fields == '' or len(fields.split(',')) == 0:
        return response_json(states=2006, message='请选择问题领域')

    # 添加 question
    new_question = Question(
        title=title,
        content=content,
        publisher=user.id,
    )

    db.session.add(new_question)
    db.session.commit()

    # 添加 question-field 关系
    fields_arr = fields.split(',')
    for field in fields_arr:
        new_question_field = QuestionField(
            question=new_question.id,
            field=field,
        )
        db.session.add(new_question_field)
        db.session.commit()

    # 创建 chatroom 房间
    chatroom_data = chatroom_create(user.username.lower(), 'MA_CR_{}'.format(new_question.id))

    if chatroom_data['res'] != 200:
        return response_json(states=1101, message='无法创建房间')

    new_question.chatroom_id = chatroom_data['msg']

    db.session.add(new_question)
    db.session.commit()

    return response_json(message='success', data={
        'id': new_question.id,
    })


@question.route('/find/')
def find():

    return render_template('question/find.html',
                           title='迷你答疑 | 寻求解答')


@question.route('/api/latest/', methods=['POST'])
def api_latest():

    try:
        token = request.cookies['token']
        page = int(request.form['page'])
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    # 判断用户合法性
    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=1002, message='token 不正确或已过期')

    # 获取数据
    questions = Question.query.order_by(
        Question.publish_time.desc()
    ).paginate(
        error_out=False,
        page=page,
        per_page=10,  # 分页; 每页 10 条
    )

    questions_data = []
    for this_question in questions.items:

        this_data = this_question.to_dict()

        questions_data.append(this_data)

    return response_json(message='success', data=questions_data)


@question.route('/api/followed/', methods=['POST'])
def api_followed():

    try:
        token = request.cookies['token']
        page = int(request.form['page'])
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    # 判断用户合法性
    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=1002, message='token 不正确或已过期')

    # 获取数据
    follow_questions = FollowQuestion.query.filter_by(
        user=user.id
    ).order_by(
        FollowQuestion.follow_time.desc()
    ).paginate(
        error_out=False,
        page=page,
        per_page=10,  # 分页; 每页 10 条
    )

    follow_questions_data = []
    for follow_question in follow_questions.items:

        this_question = Question.query.filter_by(id=follow_question.question).first()
        this_data = this_question.to_dict()

        follow_questions_data.append(this_data)

    return response_json(message='success', data=follow_questions_data)


@question.route('/<int:question_id>/')
def display(question_id):

    current_user = None

    try:
        token = request.cookies['token']
    except KeyError:
        current_user = None

    current_user = User.verify_auth_token(token)

    this_question = Question.query.filter_by(id=question_id).first()

    if question_id is None or this_question is None:
        abort(404)

    # 增加浏览量
    this_question.viewed += 1

    db.session.add(this_question)
    db.session.commit()

    # 取问题信息
    question_data = this_question.to_dict()

    # 判断用户是否已经关注问题
    is_user_followed = False

    if current_user is not None:
        follow_question = FollowQuestion.query.filter(
            and_(FollowQuestion.question == question_id, FollowQuestion.user == current_user.id)
        ).all()
        is_user_followed = (len(follow_question) == 1)

    question_data['is_user_followed'] = is_user_followed

    return render_template('question/display.html',
                           title=this_question.title,
                           question=this_question,
                           meta_data='<script>var questionData = %s;</script>' % dumps(question_data))


@question.route('/<int:question_id>/whiteboard/')
def whiteboard(question_id):

    try:
        token = request.cookies['token']
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    # 判断用户合法性
    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=1002, message='token 不正确或已过期')

    this_question = Question.query.filter_by(id=question_id).first()

    if question_id is None or this_question is None:
        abort(404)

    # 取问题信息
    question_data = this_question.to_dict()

    # 判断问题是否已被回答
    answer = Answer.query.filter_by(question=question_id).first()
    if answer is not None:
        return response_json(states=3003, message='问题已被回答')

    # 取提问者信息
    publisher = User.query.filter_by(id=this_question.publisher).first()

    return render_template('question/whiteboard.html',
                           title=this_question.title,
                           question=this_question,
                           publisher=safe_user_dict(publisher),
                           user=user,
                           meta_data='<script>var questionData = %s;</script>' % dumps(question_data))


@question.route('/follow/', methods=['POST'])
def follow():

    try:
        token = request.cookies['token']
        question_id = int(request.form['question_id'])
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    # 判断用户合法性
    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=1002, message='token 不正确或已过期')

    # 取问题
    this_question = Question.query.filter_by(id=question_id).first()

    if this_question is None:
        return response_json(states=1003, message='问题不存在')

    # 取 follow-question 关系
    follow_question = FollowQuestion.query.filter(
        and_(FollowQuestion.question == question_id, FollowQuestion.user == user.id)
    ).all()

    if len(follow_question) != 0:
        return response_json(states=1004, message='已经关注该问题')

    new_follow_question = FollowQuestion(
        question=question_id,
        user=user.id,
    )

    db.session.add(new_follow_question)
    db.session.commit()

    return response_json(message='success')


@question.route('/unfollow/', methods=['POST'])
def unfollow():

    try:
        token = request.cookies['token']
        question_id = int(request.form['question_id'])
    except KeyError:
        return response_json(states=1001, message='信息不完整')

    # 判断用户合法性
    user = User.verify_auth_token(token)

    if user is None:
        return response_json(states=1002, message='token 不正确或已过期')

    # 取问题
    this_question = Question.query.filter_by(id=question_id).first()

    if this_question is None:
        return response_json(states=1003, message='问题不存在')

    # 取 follow-question 关系
    follow_question = FollowQuestion.query.filter(
        and_(FollowQuestion.question == question_id, FollowQuestion.user == user.id)
    ).all()

    if len(follow_question) == 0:
        return response_json(states=1004, message='没有关注该问题')

    db.session.delete(follow_question[0])
    db.session.commit()

    return response_json(message='success')


