# -*- coding: utf-8 -*-
import datetime

import time
import calendar
def strTodatetime(datestr, format):
 return datetime.datetime.strptime(datestr, format)
print (time.strftime("%Y-%m-%d", time.localtime()))
print (strTodatetime("2014-3-1","%Y-%m-%d"))
print (time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()))
print (strTodatetime("2005-2-16","%Y-%m-%d")-strTodatetime("2004-12-31","%Y-%m-%d"))

#rili
cal = calendar.month(2015, 12)
print (cal)
calendar.setfirstweekday(calendar.SUNDAY) # 设置日历的第一天
cal = calendar.month(2015, 12)
print (cal)
 
# 获取一年的日历
cal = calendar.calendar(2015)
print (cal)
cal = calendar.HTMLCalendar(calendar.MONDAY)
print (cal.formatmonth(2015, 12))

dayOfWeek =datetime. datetime.now().weekday()
print (dayOfWeek)
print (time.localtime())
a12= datetime.date.isoweekday(datetime.date.today())
dayOfWeek = datetime.datetime.today().weekday()
print (dayOfWeek)
print (a12)
nowTime=datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')#现在
pastTime = (datetime.datetime.now()-datetime.timedelta(hours=1)).strftime('%Y-%m-%d %H:%M:%S')#过去一小时时间
afterTomorrowTime = (datetime.datetime.now()+datetime.timedelta(days=2)).strftime('%Y-%m-%d %H:%M:%S')#后天
tomorrowTime = (datetime.datetime.now()+datetime.timedelta(days=1)).strftime('%Y-%m-%d %H:%M:%S')#明天
print('\n',nowTime,'\n',pastTime,'\n',afterTomorrowTime,'\n',tomorrowTime)
