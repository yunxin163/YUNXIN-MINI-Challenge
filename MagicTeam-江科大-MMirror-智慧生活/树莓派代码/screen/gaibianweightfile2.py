#!/usr/bin/env python
# -*- coding: utf8 -*-

#import ccxt
import time
import RPi.GPIO as GPIO
import wenshidu
import requests
import json
import sys
import smoke
continue_reading = True

def gexin():
        
        while continue_reading:      
                apiurl = 'http://101.132.169.177/magicmirror/genxin/jsonshuju.php'
                apiheaders = {'U-ApiKey': 'aQhiPsTqV9jHnnE7','content-type':'application/json'}
                
                username ='1441903116'
                yanwu = smoke.smoke()
                jifen = str(wenshidu.wenshi())
                
                payload={'username':username,'yanwu':smoke,'jifen':jifen}
             
                r = requests.post(apiurl, headers=apiheaders, data=json.dumps(payload))

                print (r.text)
                time.sleep(5)
while True:
        gexin()
        

   
            
     
