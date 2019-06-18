#!/usr/bin/python3
# -*- coding: utf-8 -*-

#import rili
#import ccxt
#import calendar
import datetime
import time
import sys
import news
import smoke
import weather
import wenshidu
import gaibianweightfile1
import schedule
import memo

#from PyQt5.QtGui import QPalette, QPixmap, QColor, QPainter
from PyQt5.QtWidgets import QWidget, QLabel, QFrame, QVBoxLayout, QApplication
#from PyQt5.QtCore import Qt
from PyQt5.QtCore import *
from PyQt5.QtGui import *
from PyQt5.QtWidgets import *
import requests
from bs4 import BeautifulSoup



class Example(QWidget):

    def __init__(self):
        super().__init__()
        
        #创建线程wenshi
        thread_wenshi = MyThread_wenshi()
        thread_wenshi.update_date.connect(self.update_wenshi) #线程发过来的信号挂接到槽：update_wenshi
        thread_wenshi.start()
        # 创建线程 news
        thread_news = MyThread_news() 
        thread_news.update_date.connect(self.update_news)
        thread_news.start()
        '''
        # 创建线程 memo
        thread_event = MyThread_memo() 
        thread_event.update_date.connect(self.update_memo)
        thread_event.start()
        '''
        # 创建线程 smoke
        thread_smoke = MyThread_smoke() 
        thread_smoke.update_date.connect(self.update_smoke)
        thread_smoke.start()
        
        #初始化窗口
        self.initUI()

    def initUI(self):
        # 背景颜色  
        col = QColor("black")
        self.setStyleSheet("QWidget { background-color: %s }" % col.name())
        
        textCol = QPalette()  
        textCol.setColor(QPalette.WindowText,Qt.white)

  
        #天气模块
        #标签-城市
        self.city = QLabel(self)  
        self.city.setAlignment(Qt.AlignCenter)
        self.city.setText('镇江')           
        self.city.setPalette(textCol)            
        self.city.setFont(QFont("Helvetica",48))            
        self.city.move(50,20)
        #标签-天气
        self.wea = QLabel(self)  
        self.wea.setAlignment(Qt.AlignCenter)
        weaCon = weather.weather()
        self.wea.setText(weaCon['type'])
        self.wea.setPalette(textCol)            
        self.wea.setFont(QFont("Helvetica",28))            
        self.wea.move(200,30)
        #标签-最高温度
        self.high = QLabel(self)  
        self.high.setAlignment(Qt.AlignCenter)
        highCon = weather.weather()
        self.high.setText(highCon['high']+'  ~')            
        self.high.setPalette(textCol)            
        self.high.setFont(QFont("Helvetica",18))            
        self.high.move(50,100)
        #标签-最低温度
        self.low = QLabel(self)  
        self.low.setAlignment(Qt.AlignCenter)
        lowCon = weather.weather()
        self.low.setText(lowCon['low'])            
        self.low.setPalette(textCol)            
        self.low.setFont(QFont("Helvetica",18))            
        self.low.move(200,100)


        #时间模块
        #标签-时间
        self.time = QLabel(self)  
        self.time.setAlignment(Qt.AlignCenter)
        self.time.setText(time.strftime("%I:%M",time.localtime()))             
        self.time.setPalette(textCol)            
        self.time.setFont(QFont("Helvetica",48))            
        self.time.move(780,20)
        #标签-当天日期
        self.date = QLabel(self)  
        self.date.setAlignment(Qt.AlignCenter)
        dateCon = str(datetime.date.today())
        self.date.setText(dateCon)            
        self.date.setPalette(textCol)            
        self.date.setFont(QFont("Helvetica",18))            
        self.date.move(825,100)
        #标签-星期数
        self.week = QLabel(self)  
        self.week.setAlignment(Qt.AlignCenter)
        dayOfWeek=str(time.strftime("%A",time.localtime()))
        self.week.setText(dayOfWeek)            
        self.week.setPalette(textCol)            
        self.week.setFont(QFont("Helvetica",16))            
        self.week.move(900,140)


        #备忘录模块
        #标签Title-Memo
        self.memo = QLabel(self)  
        self.memo.setText('备忘录')            
        self.memo.setPalette(textCol)            
        self.memo.setFont(QFont("Helvetica",28))
        self.memo.move(50,220)
        #memodata
        data_memo=memo.memo()
        #memo-content
        self.memoCont = QLabel(self)  
        self.memoCont.setText(data_memo[2])            
        self.memoCont.setPalette(textCol)            
        self.memoCont.setFont(QFont("Helvetica",18))
        self.memoCont.move(50,300)


        #课程表模块
        #标签Title-Schedule
        self.sch_tt = QLabel(self)  
        self.sch_tt.setText('课程表')            
        self.sch_tt.setPalette(textCol)            
        self.sch_tt.setFont(QFont("Helvetica",28))
        self.sch_tt.move(820,220)
        #scheduledata
        data_sch = schedule.schedule()
        #schedule-first第一节
        self.sch1 = QLabel(self)   
        self.sch1.setText('第一节: ' +data_sch[0])
        self.sch1.setPalette(textCol)            
        self.sch1.setFont(QFont("Helvetica",18))
        self.sch1.move(720,300)
        #schedule-second
        self.sch2 = QLabel(self)   
        self.sch2.setText('第二节: ' +data_sch[1])
        self.sch2.setPalette(textCol)            
        self.sch2.setFont(QFont("Helvetica",18))
        self.sch2.move(720,340)
        #schedule-third
        self.sch3 = QLabel(self)   
        self.sch3.setText('第三节: ' +data_sch[2])
        self.sch3.setPalette(textCol)            
        self.sch3.setFont(QFont("Helvetica",18))
        self.sch3.move(720,380)
        #schedule-fourth
        self.sch4 = QLabel(self)   
        str4 = data_sch[3]
        self.sch4.setText('第四节: '+data_sch[3])
        self.sch4.setPalette(textCol)            
        self.sch4.setFont(QFont("Helvetica",18))
        self.sch4.move(720,420)
        #schedule-fifth
        self.sch5 = QLabel(self)   
        str5 = data_sch[4]
        self.sch5.setText('第五节: '+data_sch[4])
        self.sch5.setPalette(textCol)            
        self.sch5.setFont(QFont("Helvetica",18))
        self.sch5.move(720,460)


        #新闻模块
        #标签Title-NEWS
        self.news_tt = QLabel(self)  
        self.news_tt.setText('News')            
        self.news_tt.setPalette(textCol)            
        self.news_tt.setFont(QFont("Helvetica",28))
        self.news_tt.move(50,600)
        #标签-新闻爬虫
        self.newsCont = QLabel(self)   
        new = news.new()
        self.newsCont.setText(new)
        self.newsCont.setPalette(textCol)            
        self.newsCont.setFont(QFont("Helvetica",18))
        self.newsCont.setGeometry(50, 650, 900, 30)
        self.newsCont.move(50,650)


        #传感器模块
        #标签-温度
        self.wen = QLabel(self)  
        self.wen.setAlignment(Qt.AlignCenter)
        wenCon=wenshidu.wenshi()
        self.wen.setText('温度:'+str(wenCon[0])+'℃')            
        self.wen.setPalette(textCol)            
        self.wen.setFont(QFont("Helvetica",18))            
        self.wen.move(50,710)
        #标签-湿度
        self.shi = QLabel(self)  
        self.shi.setAlignment(Qt.AlignCenter)
        shiCon=wenshidu.wenshi()
        self.shi.setText('湿度:'+str(shiCon[1])+'%')
        self.shi.setPalette(textCol)            
        self.shi.setFont(QFont("Helvetica",18))            
        self.shi.move(860,710)
        #标签-烟雾
        self.smoke = QLabel(self)  
        yan=smoke.smoke()
        self.smoke.setText('烟雾:' + yan)            
        self.smoke.setPalette(textCol)            
        self.smoke.setFont(QFont("Helvetica",18))
        self.smoke.move(460,710)

             
        #新建一个QTimer对象        
        self.timer = QTimer()      
        self.timer.setInterval(1000)       
        self.timer.start()
        # 信号连接到槽       
        self.timer.timeout.connect(self.onTimerOut)
        
        
        #绝对定位
        self.setGeometry(0, 0, 1024, 768)
        self.setWindowTitle('绝对定位')        
        self.show()


    #更新爬新闻
    def update_news(self, data):
        self.newsCont.setText(data)
        '''
    #更新Memo
    def update_memo(self, data):
        self.memo.setText(data)
        '''
    #更新温度湿度
    def update_wenshi(self, data):
        self.wen.setText('温度:'+data[0]+ '℃')
        self.shi.setText('湿度:'+data[1]+ '%')
    #更新smoke 
    def update_smoke(self, data):
        self.smoke.setText('烟雾:'+data)
    
    #close
    def keyPressEvent(self, e):
        if e.key() == Qt.Key_Escape:
            self.close()
    
    #定义槽时间
    def onTimerOut(self):
        self.label1.setText(time.strftime("%H:%M",time.localtime())) 
    

#线程爬新闻
class MyThread_news(QThread):
        #通过类成员对象定义信号
        update_date = pyqtSignal(str) #pyqt5支持python3的str,没有Qstring
        #处理业务逻辑
        def run(self):
            newsHTML = requests.get("http://news.sina.com.cn/world/")
            newsHTML.encoding = "utf-8"
            soup = BeautifulSoup(newsHTML.text,"html.parser")
            for newsItem in soup.select(".news-item"):
                if len(newsItem.select("h2"))>0:
                    title = newsItem.select("h2")[0].text
                    self.update_date.emit(title)  # 发射信号
                    time.sleep(5)
'''
#定义线程,备忘录
class MyThread_memo(QThread):
        update_date = pyqtSignal(str)
        def run(self):
            while True:
                text_event=gaibianweightfile1.gexin()
                self.update_date.emit(str(Con_memo))
                time.sleep(3600)
'''
#定义线程,温湿度
class MyThread_wenshi(QThread):
        update_date = pyqtSignal(str)
        def run(self):
            while True:
                text_wenshi=wenshidu.wenshi()
                self.update_date.emit(str(text_wenshi))
                time.sleep(1)
                
class MyThread_smoke(QThread):
        update_date = pyqtSignal(str)
        def run(self):
            while True:
                text_smoke=smoke.smoke()
                self.update_date.emit(str(text_smoke))
                time.sleep(1)
       


if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = Example()
    sys.exit(app.exec_())
