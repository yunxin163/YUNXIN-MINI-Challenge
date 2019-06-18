import requests
from bs4 import BeautifulSoup
def new():
    newsHTML = requests.get("http://news.sina.com.cn/world/")
    newsHTML.encoding = "utf-8"
    soup = BeautifulSoup(newsHTML.text,"html.parser")
    for newsItem in soup.select(".news-item"):
        if len(newsItem.select("h2"))>0:
            title = newsItem.select("h2")[0].text
            
            return title
    
