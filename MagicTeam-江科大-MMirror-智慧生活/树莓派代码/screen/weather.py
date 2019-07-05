#!python3  
#coding:utf-8  
import json, sys, requests  
   
#输入地点
def weather():
    weatherPlace = "镇江"  

    #下载天气JSON  
    weatherJsonUrl = "http://wthrcdn.etouch.cn/weather_mini?city=%s" % (weatherPlace)  
    response = requests.get(weatherJsonUrl)  
    try:  
        response.raise_for_status()  
    except:  
        print("网址请求出错")  
      
    #将json文件格式导入成python的格式  
    weatherData = json.loads(response.text)
    #get today's weather information
    data = weatherData['data']['forecast'][0]
    
    return data

#print (weather())
