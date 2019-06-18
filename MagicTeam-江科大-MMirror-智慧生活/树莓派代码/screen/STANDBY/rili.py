import calendar
import datetime

import time

def rili():
    year = datetime.datetime.now().year
    month = datetime.datetime.now().month
    cal = calendar.month(year,month)
    return cal
#print (rili())
