import requests
import os,time
def upPicture():
    os.system('raspistill -o 1.jpg -t 1000 -n')
    print("pic success")
    url = "http://101.132.169.177/testPic/upImage.php"
    files = {'file':('1.jpg', open('/home/pi/magicMirror/zhinengjing/screen/1.jpg','rb'),'image/jpeg')}
    res = requests.post(url,files=files)
    print(res.text)
#upPicture()