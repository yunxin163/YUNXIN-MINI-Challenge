#!/usr/bin/env python
# -*- coding: utf8 -*-

import requests
import json
#import urllib2
def schedule(): 
    url = 'http://101.132.169.177/magicmirror/genxin/get_schedule.php'
    r = requests.get(url)
    r.raise_for_status()
    r.encoding = r.apparent_encoding
    result = json.loads(r.text)
    #print(result['first'])S
    first = str(result['first'])
    second = str(result['second'])
    third = str(result['third'])
    fourth = str(result['fourth'])
    fifth = str(result['fifth'])

    #print(second_arr)
    data = [first,second,third,fourth,fifth]
    #Sprint (first)
    return data
#schedule()

