import numpy as np
import argparse
import imutils
import cv2
import os

ap = argparse.ArgumentParser()
ap.add_argument("-c", "--cascade", type=str, default="haarcascade_frontalface_default.xml", help="path to where the face cascade resides")
ap.add_argument("-v", "--image", type=str, default='./images/jzm.jpg', help="path to the image")
args = vars(ap.parse_args())

detector = cv2.CascadeClassifier(args["cascade"])

frame = cv2.imread(args['image'])
# frame = imutils.resize(frame, width=300)
gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

rects = detector.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30), flags=cv2.CASCADE_SCALE_IMAGE)

for (fX, fY, fW, fH) in rects:
    cv2.imwrite('jzm_f.jpg', frame[fY: fY + fH, fX: fX + fW])
    cv2.rectangle(frame, (fX, fY), (fX + fW, fY + fH), (0, 0, 255), 2)

cv2.imshow("Face", frame)
cv2.waitKey(0)
cv2.destroyAllWindows()