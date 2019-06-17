import re
import time
from flask import jsonify


def response_json(states=200, message=None, data=None, token=None, timestamp=int(round(time.time() * 1000))):
    resp = jsonify({'states': states,
                    'message': message,
                    'data': data,
                    'token': token,
                    'timestamp': timestamp}
                   )
    # resp.headers['Access-Control-Allow-Credentials'] = 'true'
    # resp.headers['Access-Control-Allow-Origin'] = '*'
    return resp


