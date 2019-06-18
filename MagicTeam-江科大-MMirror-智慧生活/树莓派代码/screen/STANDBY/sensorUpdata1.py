#!/user/bin/python
# -*- coding: UTF-8 -*-

import mysqldb
import time

continueUpdata = True

def updata():
    
    while continueUpdata:
        
        smokedata = smoke.smoke()
        wenshidata = wenshidu.wenshi()
        temperature = wenshidata[0]
        humidity = wenshidata[1]
        
        db.MySQLdb.connect("101.1132.169.177","root","crb","myDB",charset='utf-8')
        cursor = db.cursor()
        sql = "updata sensor set temperature='%d',humidity='%d',smoke='%s' where id='162210702113'" % (temperature,humidity,smoke)
        
        try:
            cursor.execute(sql)
            db.commit()
        except:
            db.rollback()
            
        db.close()
        
        time.sleep(5)

while True:
        updata()