#testing file for flame.py -- liyana
#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import json
import urllib3




# GPIO SETUP
channel = 17#pin 17 -- fire sensor
GPIO.setmode(GPIO.BCM)#specify the sensor to use the broadcom mode in Pi
GPIO.setup(channel, GPIO.IN)#set pin 17 as input port -- fire sensor


def calluser():
    data = {'error': False, 'fire_detect': True}
    encoded_data = json.dumps(data).encode('utf-8')
    http = urllib3.PoolManager()
    http.request('GET', 'http://192.168.43.33/smartfire/api/public/api/mobile/callUser')
    #stat = json.loads(r.data)
    #print(stat)
    #r = http.request(
    #'POST',
    #'http://192.168.0.56/smartdbbox/api/public/api/notify/trigger',
    #body=encoded_data,
    #headers={'Content-Type': 'application/json'})

def callback(channel):
    
    print ('flame detect')
    #print(json.loads(r.data))
 
#GPIO.add_event_detect(channel, GPIO.BOTH, bouncetime=300)  # let us know when the pin goes HIGH or LOW
#GPIO.add_event_callback(channel, callback)  # assign function to GPIO PIN, Run function on change
 
def onbuzzer():
    GPIO.setup(23, GPIO.OUT)#set pin 23 as output port -- relay port
    GPIO.output(23, GPIO.LOW)#ON the buzzer -- ON relay
    GPIO.output(23, GPIO.HIGH)#OFF the buzzer -- OFF relay
    #time.sleep(0.25)#idle thread -- time sleep
    #GPIO.cleanup()#clear GPIO status/traffic
