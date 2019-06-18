#!/usr/bin/env python
# -*- coding: utf8 -*-

import requests
import json
#import urllib2
def memo(): 
    url = 'http://101.132.169.177/magicmirror/genxin/memo.php'
    r = requests.get(url)
    r.raise_for_status()
    r.encoding = r.apparent_encoding
    result = json.loads(r.text)
    #print(result['first'])
    type = str(result['type'])
    title = str(result['title'])
    content = str(result['content'])
    datetime = str(result['datetime'])
    remindtime = str(result['remindtime'])
    data = [type,title,content,datetime,remindtime]
    
    return data
