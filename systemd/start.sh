#!/bin/bash
sudo pon test1;
sleep 10s;
sudo route add default dev ppp0;
sudo python3.6 /home/pi/smartdb/main.py;
echo "done";