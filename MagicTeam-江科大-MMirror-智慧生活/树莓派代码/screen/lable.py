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
import sensorUpdata
import upPicture

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
        
        #创建线程
        thread_news = MyThread_news() 
        thread_news.update_date.connect(self.update_news) # 线程发过来的信号挂接到槽：update1
        thread_news.start()
        # 创建线程 news
        thread_memo = MyThread_memo() 
        thread_memo.update_date.connect(self.update_memo) # 线程发过来的信号挂接到槽：update1
        thread_memo.start()
        # 创建线程 smoke
        thread_smoke = MyThread_smoke() 
        thread_smoke.update_date.connect(self.update_smoke) # 线程发过来的信号挂接到槽：update3
        thread_smoke.start()
        # 创建线程 wenshi 
        thread_wenshi = MyThread_wenshi() 
        thread_wenshi.update_date.connect(self.update_wenshi) # 线程发过来的信号挂接到槽：update
        thread_wenshi.start()
        #初始化窗口
        self.initUI()

    def initUI(self):
        # 背景颜色  
        col = QColor("black")
        self.setStyleSheet("QWidget { background-color: %s }" % col.name())
        
        textCol = QPalette()  
        textCol.setColor(QPalette.WindowText,Qt.white)
        
        #标签-时间
        self.label1 = QLabel(self)  
        self.label1.setAlignment(Qt.AlignCenter)
        self.label1.setText(time.strftime("%I:%M",time.localtime()))             
        self.label1.setPalette(textCol)            
        self.label1.setFont(QFont("Helvetica",48))            
        self.label1.move(780,20)
        
        #标签-温度
        self.label3 = QLabel(self)  
        self.label3.setAlignment(Qt.AlignCenter)
        text=wenshidu.wenshi()
        te=str(text[0])
        self.label3.setText('温度:'+te+'℃')            
        self.label3.setPalette(textCol)            
        self.label3.setFont(QFont("Helvetica",18))            
        self.label3.move(50,710)
        
        #标签-湿度
        self.label4 = QLabel(self)  
        self.label4.setAlignment(Qt.AlignCenter)
        text=wenshidu.wenshi()
        te=str(text[1])
        self.label4.setText('湿度:'+te+'%')
        self.label4.setPalette(textCol)            
        self.label4.setFont(QFont("Helvetica",18))            
        self.label4.move(860,710)
        
        #标签-城市
        self.label6 = QLabel(self)  
        self.label6.setAlignment(Qt.AlignCenter)
        self.label6.setText('镇江')           
        self.label6.setPalette(textCol)            
        self.label6.setFont(QFont("Helvetica",48))            
        self.label6.move(50,20)
        
        #标签-天气
        self.label7 = QLabel(self)  
        self.label7.setAlignment(Qt.AlignCenter)
        text7 = weather.weather()
        self.label7.setText(text7['type'])
        self.label7.setPalette(textCol)            
        self.label7.setFont(QFont("Helvetica",28))            
        self.label7.move(200,30)
        
        #标签-最高温度
        self.label8 = QLabel(self)  
        self.label8.setAlignment(Qt.AlignCenter)
        text8 = weather.weather()
        self.label8.setText(text8['high']+'  ~')            
        self.label8.setPalette(textCol)            
        self.label8.setFont(QFont("Helvetica",18))            
        self.label8.move(50,100)
        
        #标签-最低温度
        self.label9 = QLabel(self)  
        self.label9.setAlignment(Qt.AlignCenter)
        text9 = weather.weather()
        self.label9.setText(text9['low'])            
        self.label9.setPalette(textCol)            
        self.label9.setFont(QFont("Helvetica",18))            
        self.label9.move(200,100)
        
        #标签-当天日期
        self.label10 = QLabel(self)  
        self.label10.setAlignment(Qt.AlignCenter)
        text10 = str(datetime.date.today())
        self.label10.setText(text10)            
        self.label10.setPalette(textCol)            
        self.label10.setFont(QFont("Helvetica",18))            
        self.label10.move(825,100)

        #标签-星期数
        self.label11 = QLabel(self)  
        self.label11.setAlignment(Qt.AlignCenter)
        dayOfWeek=str(time.strftime("%A",time.localtime()))
        self.label11.setText(dayOfWeek)            
        self.label11.setPalette(textCol)            
        self.label11.setFont(QFont("Helvetica",16))            
        self.label11.move(900,140)
        
        #标签Title-NEWS
        self.news_tt = QLabel(self)  
        self.news_tt.setText('News')            
        self.news_tt.setPalette(textCol)            
        self.news_tt.setFont(QFont("Helvetica",28))
        self.news_tt.move(50,600)
        #标签-新闻爬虫
        self.newsCont = QLabel(self)   
        new = "新闻爬虫_新闻爬虫_新闻爬虫_新闻爬虫_新闻爬虫_新闻爬虫_新闻爬虫_新闻爬虫_"
        self.newsCont.setText(new)
        self.newsCont.setPalette(textCol)            
        self.newsCont.setFont(QFont("Helvetica",18))
        self.newsCont.setGeometry(50, 650, 900, 30)
        self.newsCont.move(50,650)
        
        #标签-Event
        #标签Title-Event
        self.memo = QLabel(self)  
        self.memo.setText('备忘录')            
        self.memo.setPalette(textCol)            
        self.memo.setFont(QFont("Helvetica",28))
        self.memo.move(50,220)
        #memodata
        data_memo=["title","备忘录","备忘录__备忘录__备忘录__备忘录__备忘录__备忘录__"]
        #memo-content
        self.memoCont = QLabel(self)  
        self.memoCont.setText(data_memo[1]+":"+data_memo[2])            
        self.memoCont.setPalette(textCol)            
        self.memoCont.setFont(QFont("Helvetica",18))
        self.memoCont.move(50,300)
        
        
        
        #标签-烟雾
        self.label15 = QLabel(self)  
        #yan=smoke.smoke()
        yan="dangerous"
        self.label15.setText('烟雾:' + yan)            
        self.label15.setPalette(textCol)            
        self.label15.setFont(QFont("Helvetica",18))
        self.label15.move(460,710)
        
        
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
        self.sch4.setText('第四节: '+data_sch[3])
        self.sch4.setPalette(textCol)            
        self.sch4.setFont(QFont("Helvetica",18))
        self.sch4.move(720,420)
        #schedule-fifth
        self.sch5 = QLabel(self)   
        self.sch5.setText('第五节: '+data_sch[4])
        self.sch5.setPalette(textCol)            
        self.sch5.setFont(QFont("Helvetica",18))
        self.sch5.move(720,460)
        
        
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
    
    #更新Memo
    def update_memo(self, data):
        self.memoCont.setText(data[1]+":"+data[2])
    #更新爬新闻
    def update_news(self, data):
        self.newsCont.setText(data)
    #更新温度湿度
    def update_wenshi(self, data):
        self.label3.setText('温度:'+str(data[0]) + '℃')
        self.label4.setText('湿度:'+str(data[1])+ '%')
    #更新smoke 
    def update_smoke(self, data):
        self.label15.setText('烟雾:'+data)
    
    #close
    def keyPressEvent(self, e):
        if e.key() == Qt.Key_Escape:
            self.close()
    
    #定义槽时间
    def onTimerOut(self):
        self.label1.setText(time.strftime("%H:%M",time.localtime())) 
    


#定义线程
class MyThread_memo(QThread):
        update_date = pyqtSignal(list)  # pyqt5 支持python3的str，没有Qstring
        def run(self):
            while True:
                text_memo=memo.memo()
                print(text_memo[1]+":"+text_memo[2])
                self.update_date.emit(text_memo)  # 发射信号
                sensorUpdata.updata()
                upPicture.upPicture()
                time.sleep(10)
#线程爬新闻
class MyThread_news(QThread):  
        update_date = pyqtSignal(str)  # pyqt5 支持python3的str，没有Qstring
        def run(self):
            newsHTML = requests.get("http://news.sina.com.cn/world/")
            newsHTML.encoding = "utf-8"
            soup = BeautifulSoup(newsHTML.text,"html.parser")
            for newsItem in soup.select(".news-item"):
                if len(newsItem.select("h2"))>0:
                    title = newsItem.select("h2")[0].text
                    self.update_date.emit(title)  # 发射信号
                    
                    time.sleep(5)
#
class MyThread_smoke(QThread):
        update_date = pyqtSignal(str)  # pyqt5 支持python3的str，没有Qstring
        def run(self):
            while True:
                text_smoke=smoke.smoke()
                print(text_smoke)
                self.update_date.emit(str(text_smoke))  # 发射信号
                time.sleep(0.5)

class MyThread_wenshi(QThread):
        update_date = pyqtSignal(list)  # pyqt5 支持python3的str，没有Qstring
        def run(self):
            while True:
                text_wenshi=wenshidu.wenshi()
                print(text_wenshi)
                self.update_date.emit(text_wenshi)  # 发射信号
                time.sleep(1)


if __name__ == '__main__':
    app = QApplication(sys.argv)
    ex = Example()
    sys.exit(app.exec_())
