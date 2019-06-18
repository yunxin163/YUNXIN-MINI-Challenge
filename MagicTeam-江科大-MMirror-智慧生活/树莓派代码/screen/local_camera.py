#coding=utf-8
import cv2

capture = cv2.VideoCapture(0)

while True:
    ret, frame = capture.read()
    cv2.imshow("camera",frame)
    if cv2.waitKey(10) == 27:
        break
    
cv2.destroyAllWindows()
capture.release()