#testing file for pi camera -- liyana

import time
from picamera import PiCamera
import datetime

camera = PiCamera()

camera.start_preview()
time.sleep(1)
time_format = datetime.datetime.now()
nameformat = time_format.strftime("%Y%m%d_%H%M%S")#eg: 20181225_
rtpath = "camera_img/cameratest___" + nameformat + ".jpg"
camera.capture(rtpath)
# camera.capture('camera_img/cameratest.jpg')
camera.stop_preview()

# camera.start_preview()
# i = 0
# while (i<5):
#     #sleep(5)
#     camera.capture('camera_img/image'+i+'.jpg')
#     i=i+1
# camera.stop_preview()

# camera.start_preview()
# camera.start_recording('camera_img/testvid.h264')
# time.sleep(10)
# camera.stop_recording()
# camera.stop_preview()