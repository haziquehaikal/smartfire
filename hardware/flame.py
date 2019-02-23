#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import schedule
import json
import urllib3
from camera import camera
from networkPreference import networkPreference
from deviceActivation import deviceActivation
from mq import MQ
from buzzer import buzzer

class flame:
    # GPIO SETUP
    channel = 17#pin 17 -- fire sensor
    GPIO.setmode(GPIO.BCM)#specify the sensor to use the broadcom mode in Pi
    GPIO.setup(channel, GPIO.IN)#set pin 17 as input port -- fire sensor

    def __init__(self):
        self.channel = 17
        self.camera = camera()
        self.network = networkPreference()
        self.device = deviceActivation()
        self.smokesensor = MQ()
        self.buzzer = buzzer()

    def startFlameRead(self):
        print('Initializing sensors reading')
        perc = self.smokesensor.MQPercentage()
        # self.buzzer.checkBuzzerStatus()

        flag = GPIO.input(self.channel)
        if(flag == 0):
            print('Fire Alert!')
            self.camera.triggerCamera()
            self.sendReport(str(perc["SMOKE"]),str(perc["GAS_LPG"]),str(perc["CO"]))
            self.buzzer.onbuzzer()
           
        else:
            print('No fire detected\n')
            print ("LPG: %g ppm, CO: %g ppm, Smoke: %g ppm" % (perc["GAS_LPG"], perc["CO"], perc["SMOKE"]))

        self.buzzer.setCurrentState(True)

    def startSmokeRead(self):
        # perc = self.smokesensor.MQPercentage()
        sys.stdout.write("\033[K")
        sys.stdout.write("LPG: %g ppm, CO: %g ppm, Smoke: %g ppm" % (perc["GAS_LPG"], perc["CO"], perc["SMOKE"]))
        sys.stdout.flush()
        time.sleep(0.1)
        print("\n")

    def sendReport(self,smoke,gas_lpg,co):
        test = json.dumps({
            "error": False,
            "firedevice_id":self.device.getDeviceID(),
            "fire_detect": True,
            "image_data":self.camera.getImageData(),
            # "image_data":"donut",
            "reading_smoke":smoke,
            "reading_gas":gas_lpg,
            "reading_lpg":co,
            "buzzer_flag":"ON",
            # "buzzer_flag":buzz
        })

        r = self.network.postData(test,"getReading")
        stat = json.loads(r.data)
        # print(test)

