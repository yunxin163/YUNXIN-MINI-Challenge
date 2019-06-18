from io import BytesIO
from time import sleep
from picamera import PiCamera

my_stream = BytesIO()
camera = PiCamera()
camera.start_preview()
sleep(2)
camera.capture(my_stream,'jpeg')
image_data = my_stream.getvalue()