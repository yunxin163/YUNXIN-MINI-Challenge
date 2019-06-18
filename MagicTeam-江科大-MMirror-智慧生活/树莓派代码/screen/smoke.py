#!/usr/bin/env python
# encoding: utf-8
import RPi.GPIO as GPIO
import time
def smoke():  
    
    CHANNEL=7
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(CHANNEL, GPIO.IN)
    while True:    
        if GPIO.input(CHANNEL)==GPIO.HIGH:
            time.sleep(1)
            msg = "fine"
            #print(msg)
            return msg
        else:
            time.sleep(1)
            msg = "dangerous"
            #print(msg)
            return msg