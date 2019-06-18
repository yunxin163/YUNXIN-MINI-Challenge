import os,time
def aotuPic():
    os.system('raspistill -o 1.jpg -t 1000')
    print("pic success")
#aotuPic()