import time
import schedule
import json
import urllib3
from networkPreference import networkPreference
import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
GPIO.setup(24, GPIO.OUT)#LED port
GPIO.setup(23, GPIO.OUT)#LED port
# GPIO.output(24, GPIO.LOW)
GPIO.output(24, GPIO.HIGH)

class deviceActivation:

    def __init__ (self):
        # 1  = ACTIVE , 0  = NOTACTIVE    
        #self.state = 0
        self.state = "NOTACTIVE"
        self.name = True
        self.deviceID = "device123"
        self.network = networkPreference()

    def checkDeviceState(self):
        schedule.every(3).seconds.do(self.checkServerJob)#execute checkServerJob every 3 sec
        #print(self.getCurrentState())
        while self.getCurrentState():
            schedule.run_pending()
            time.sleep(1)

            
    def getDeviceID(self):
        return self.deviceID

    def setCurrentState(self,status):
        self.name = status
    
    def getCurrentState(self):
        return self.name

    def checkServerJob(self):
        
        print("Checking device activation status..")
        
        test = json.dumps({
            "error": False,
            "firedevice_id":"device123"
        })

        r = self.network.postData(test,"checkStatus")
        
        stat = json.loads(r.data)
        
        if(stat['error'] == False):
            if(stat['status'] == "ACTIVE"):
                #device is activated
                self.setCurrentState(False)
                print("Device is activated..")
                print("\nDevice checking task will be ended")
                # GPIO.setup(23, GPIO.OUT)#LED port
                GPIO.output(24, GPIO.LOW)
                time.sleep(1)
                GPIO.output(23, GPIO.HIGH)

                schedule.clear() #stop the schedule -- looping
            else:
                #device is not activated
                print("Device is not activated..")

                pass
        else:
            print(stat['message'])


  
        

