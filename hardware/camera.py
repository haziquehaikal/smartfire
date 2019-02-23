import time
from picamera import PiCamera
import base64
import datetime

class camera():
    
    def __init__(self):
        self.camera=PiCamera()
        self.image_data = ''
        self.camera.start_preview()

    def triggerCamera(self):
        # self.camera.start_preview()
        time.sleep(2)
        time_format = datetime.datetime.now()
        nameformat = time_format.strftime("%Y%m%d_%H%M%S")#eg: 20181225_
        rtpath = "camera_img/" + nameformat + ".jpg"
        self.camera.capture(rtpath)
        self.camera.stop_preview()
        self.imageConvert(rtpath)

    def getImageData(self):
        return self.image_data

    def imageConvert(self,image_data):
        image = open(image_data, 'rb')  #open binary file in read mode (rb == read binary)
        image_read = image.read()
        image_64_encode = base64.b64encode(image_read)
        strdata = str(image_64_encode).strip('b')#buang b dalam base64
        trimdata = ''
        for line in strdata:
            trimdata += line.strip("'")#buang space dalam base64
        final_data = 'data:image/jpeg;base64,' + trimdata#append the trimmed data
        self.image_data = final_data
        # print(self.image_data)
        # return final_data
    
