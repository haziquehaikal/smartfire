#!/usr/bin/env python

import time

import RPi.GPIO as GPIO


GPIO.setmode(GPIO.BCM)#specify the sensor to use the broadcom mode in Pi 
GPIO.setup(23, GPIO.OUT)#set pin 23 as output port
GPIO.output(23, GPIO.LOW)#OFF the buzzer

time.sleep(0.25)#idle thread

GPIO.output(23, GPIO.HIGH)#ON the buzzer
GPIO.cleanup()#clear GPIO status/traffic