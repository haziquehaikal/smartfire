import sys, time
import json
import time
import RPi.GPIO as GPIO
from deviceActivation import deviceActivation
from flame import flame

try:
    print("Press CTRL+C to abort.")
    
    activationserver = deviceActivation()

    while True:
        activationserver.checkDeviceState()
        flamesensor = flame()
        
        while activationserver.getCurrentState() == False:
            flamesensor.startFlameRead()

        if(activationserver.getCurrentState == True):
            print("Device is not active")
            pass
except:
    print("\nAbort by user")
    GPIO.cleanup()#stop the buzzer -- clear the GPIO pin that is running


