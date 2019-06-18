import time
import RPi.GPIO as GPIO
continue_reading = True
GPIO.setmode(GPIO.BOARD)
GPIO.setup(37,GPIO.OUT)
GPIO.setup(35,GPIO.IN)
def hx711read():
	#未经1441903116曹润彬本人同意不得二传
        GPIO.setmode(GPIO.BOARD)
        GPIO.setup(37,GPIO.OUT)
        GPIO.setup(35,GPIO.IN)
        GPIO.output(37,GPIO.LOW)
        time.sleep(0.000002)
        count=0
        
        while GPIO.input(35):
                pass
       
        i=0
        while i<24:
                GPIO.output(37,GPIO.HIGH) 
                count=count<<1
                
                i=i+1
                GPIO.output(37,GPIO.LOW)
                if GPIO.input(35):
                        count=count+1
                        
        pass

               
        GPIO.output(37,GPIO.HIGH)
        
        count=count^0x800000
        
        time.sleep(0.000001)
        GPIO.output(37,GPIO.LOW)
        asd= (count-8320000)/210
        return (count-8320000)/210
        print (count-8320000)/210
        
        GPIO.cleanup()
jifn = hx711read()
print (jifn)
