#!/usr/bin/env python
#import PCF8591 as ADC
import RPi.GPIO as GPIO
import time
import math
from smbus2 import SMBus


bus = SMBus(1)

DO = 17
Buzz = 18
GPIO.setmode(GPIO.BCM)


def setup():
    # ADC.setup(0x48)
    GPIO.setup(DO, 	GPIO.IN)
    GPIO.setup(Buzz, 	GPIO.OUT)
    GPIO.output(Buzz,	1)


def Print(x):
    if x == 1:
        print('')
        print('   *********')
        print('   * Safe~ *')
        print('   *********')
        print('')
    if x == 0:
        print('')
        print('   ***************')
        print('   * Danger Gas! *')
        print('   ***************')
        print('')


def BoardRes(raw_adc):
    return float(5*(1023.0-raw_adc)/float(raw_adc))


def loop():
    status = 1
    count = 0

    while True:

        bus.write_byte(0x48, 0x40)
        adcval = bus.read_byte(0x48)
        print(adcval)
        print(BoardRes(adcval))

        tmp = GPIO.input(DO)
        if tmp != status:
            Print(tmp)
            status = tmp
        if status == 0:
            count += 1
            if count % 2 == 0:
                GPIO.output(Buzz, 1)
            else:
                GPIO.output(Buzz, 0)
        else:
            GPIO.output(Buzz, 1)
            count = 0

        time.sleep(0.2)


def destroy():
    GPIO.output(Buzz, 1)
    GPIO.cleanup()


if __name__ == '__main__':
    try:
        setup()
        loop()
    except KeyboardInterrupt:
        destroy()
