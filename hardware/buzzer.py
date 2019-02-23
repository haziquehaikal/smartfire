#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import schedule
import json
from networkPreference import networkPreference


class buzzer():
    
    def __init__(self):        
        self.status = True
        self.state="OFF"
        self.network=networkPreference()
    
    def checkBuzzerStatus(self):
        elapsed = 0
        start = time.time()

        schedule.every(3).seconds.do(self.checkBuzzer)#execute checkBuzzer every 3 sec

        while self.getCurrentState():
            if(elapsed < 20):
                schedule.run_pending()
                time.sleep(1)
                
                elapsed = time.time() - start
            else:
                self.setCurrentState(False)
                schedule.clear()
                self.callAuth()
                # self.callUser()
               
    
    def setCurrentState(self,s):
        self.status = s
    
    def getCurrentState(self):
        return self.status

    def checkBuzzer(self):
        
        print("Checking buzzer deactivation trigger..")
        
        test = json.dumps({
            # "error": False,
            "firedevice_id":"device123"
        })

        r = self.network.postData(test,"checkBuzzer")
        
        stat = json.loads(r.data)
        # buzzer = FALSE ---- TURN OFF
        # buzzer = TRUE ----- TURN ON
        
        if(stat['error'] == False):
            if(stat['buzzer'] == True):
                # BUZZER ON
                pass               
            else:
                # BUZZER OFF
                self.setCurrentState(False)
                self.offbuzzer()
        else:
            print(stat['message'])

    def onbuzzer(self):
        GPIO.setup(18, GPIO.OUT)#set pin 23 as output port -- relay port
        GPIO.output(18, GPIO.LOW)#OFF the buzzer -- OFF relay
        GPIO.output(18, GPIO.HIGH)#ON the buzzer -- ON relay
        
        self.checkBuzzerStatus()
        
    def offbuzzer(self):
        GPIO.output(18, GPIO.LOW)#OFF the buzzer -- OFF relay
        schedule.clear() #stop the schedule -- looping
        GPIO.cleanup(18)#clear GPIO status/traffic

    def callAuth(self):
        print("Calling Authority due to user inactivity")
        r = self.network.getData("callAuth")
        