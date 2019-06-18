#! /usr/bin/python
# coding:utf-8
import urllib.request
import json

def weather():
    url= "http://www.weather.com.cn/data/sk/101190301.html"
    html=urllib.request.urlopen(url).read()
    data_s=html.decode('utf-8')
    data=json.loads(data_s)
    info1=data['weatherinfo']
    
    print(info1)
    print('城市：%s'%info1['city'])
    print('温度：%s度'%info1['temp'])
    print('风速：%s'%info1['WD'],info1['WS'])
    print('湿度：%s'%info1['SD'])
    print('时间：%s'%info1['time'])
    
    ApiUrl= "http://www.weather.com.cn/data/cityinfo/101190301.html"
    html=urllib.request.urlopen(ApiUrl)
#读取并解码
    data=html.read().decode("utf-8")
#将JSON编码的字符串转换回Python数据结构
    ss=json.loads(data)
    info=ss['weatherinfo']
    
    print('城市：%s'+ info['city'])
    print('温度：%s度'%info['temp1'])
    print('温度：%s度'%info['temp2'])
    print('tianqi：%s度'%info['weather'])
    
    return info
   
#print('温度：%s度'%info['n0.gif'])



