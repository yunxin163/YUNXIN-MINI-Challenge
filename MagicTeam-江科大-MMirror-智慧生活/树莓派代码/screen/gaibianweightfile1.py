#!/usr/bin/env python
# -*- coding: utf8 -*-

#import ccxt
import time
import RPi.GPIO as GPIO
import wenshidu
import requests
import json
import sys
continue_reading = True

def gexin():
        
        while continue_reading:  
		#在使用是apiurl要根据服务器地址修改才能使用
                apiurl = 'http://101.132.169.177/magicmirror/genxin/json.php'
                apiheaders = {'U-ApiKey': 'aQhiPsTqV9jHnnE7','content-type':'application/json'}
                
                uid ='caonima'
                key= '1456'       
                #jifen=round(ccxt.hx711read(),1)
                #jifen=str(jifen)
                jifen = str(wenshidu.wenshi())
                payload={'username':uid,'key':key,'jifen':jifen}
             
                r = requests.post(apiurl, headers=apiheaders, data=json.dumps(payload))
                #print (r.text[10:100])
                rt=r.text[17:]
                rt =rt.encode('latin-1').decode('unicode-escape')
                return rt
        

   
            
     
