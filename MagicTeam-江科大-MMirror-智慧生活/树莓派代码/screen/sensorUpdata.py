#!/usr/bin/env python
# -*- coding: utf8 -*-

#import ccxt
import time
import wenshidu
import smoke
import urllib.request
import urllib.parse

continueUpdata = True

def updata():
        
        #while continueUpdata:
                
                url = 'http://101.132.169.177/magicmirror/genxin/sensorUpdata.php'
                #apiheaders = {'U-ApiKey': 'aQhiPsTqV9jHnnE7','content-type':'application/json'}
                
                smokedata = smoke.smoke()
                wenshidata = wenshidu.wenshi()
                temperature = wenshidata[0]
                humidity = wenshidata[1]
                params = urllib.parse.urlencode({'temperature':temperature,'humidity':humidity,'smoke':smokedata })
             
                f = urllib.request.urlopen("http://101.132.169.177/magicmirror/genxin/get_sensor.php?%s" % params)
                print(f.read().decode('utf-8'))
                #time.sleep(60)
#while True:
        #updata()