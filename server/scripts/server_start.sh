#!/usr/bin/env bash
cd /home/linux/build
sudo nohup java -jar server-0.0.1-SNAPSHOT.jar --spring.profiles.active=server > /dev/null 2> /dev/null < /dev/null &