#testing file for sensors' readings -- liyana
from mq import *
from flame_t import *
import sys, time
import urllib3 #networking library
import json
import datetime
import base64
import time
from picamera import PiCamera
from deviceActivation import deviceActivation


def convert(path):

    # print('convert')
    image = open(path, 'rb')  # open binary file in read mode
    image_read = image.read()
    image_64_encode = base64.b64encode(image_read)
    strdata = str(image_64_encode).strip('b')
    trimdata = ''
    for line in strdata:
        trimdata += line.strip("'")
    #send(trimdata)
    print('data:image/jpeg;base64,' + trimdata)

try:
    print("Press CTRL+C to abort.")

    #mq = MQ();
    
    camera = PiCamera()

    camera.start_preview()

    time_format = datetime.datetime.now()

    activationserver = deviceActivation()
    
    while True:
        activationserver.checkDeviceState()
        while activationserver.getCurrentState() == False:

            flag = GPIO.input(channel)#flame sensor's input
            if(flag == 0):#ada fire
            
                print("Fire Alert!")
                onbuzzer()
                path = time_format.strftime("%Y%m%d_%H%M%S")#eg: 20181225_                
                #time.sleep(5)
                rtpath = "camera_img/" + path + ".jpg"
                camera.capture(rtpath)
                camera.stop_preview()
                image = open(rtpath, 'rb')  #open binary file in read mode
                image_read = image.read()
                image_64_encode = base64.b64encode(image_read)
                strdata = str(image_64_encode).strip('b')
                trimdata = ''
                for line in strdata:
                    trimdata += line.strip("'")
                #send(trimdata)
                print('data:image/jpeg;base64,' + trimdata)
            
            print("Fire Flag: ")
            print(flag)
            #print("\n")
            #time.sleep(1)

            # http = urllib3.PoolManager()
            # perc = mq.MQPercentage()
            # sys.stdout.write("\r")
            # sys.stdout.write("\033[K")
            # data = {
            #     'reading_smoke':str(perc["SMOKE"]),
            #     'reading_gas':str(perc["GAS_LPG"]),
            #     'reading_heat':str(perc["CO"]),
            #     #'error':FALSE

            #     #'reading_smoke':str("test"),
            #     #'reading_gas':str("test"),
            #     #'reading_heat':str("test"),
            # }
            # encoded_data = json.dumps(data).encode('utf-8')#create JSON object
            # http.request(
            #     'POST',
            #     'http://192.168.43.33/smartfire/api/public/api/mobile/getReading',#IP add server
            #     body=encoded_data,
            #     headers={'Content-Type': 'application/json'} )
            # #json.loads(r.data.decode('utf-8'))['json']
            # sys.stdout.write("LPG: %g ppm, CO: %g ppm, Smoke: %g ppm" % (perc["GAS_LPG"], perc["CO"], perc["SMOKE"]))
            # sys.stdout.flush()
            time.sleep(0.1)
            print("\n")

        if(activationserver.getCurrentState == True):
            print("Device is not active")
            break
except:
    print("\nAbort by user")
    GPIO.cleanup()#stop the buzzer -- clear the GPIO pin that is running
