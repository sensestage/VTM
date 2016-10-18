# VTM
VTM - system for Verdensteatret

###requirements

* UnitTesting quark
* sc3-plugins

###install VTM

* Install supercollider
* `git clone https://github.com/blacksound/VTM.git`
* Add the path to the VTM folder under `- includePaths` in the `sclang_conf.yaml` file.
  - Run `Platform.userAppSupportDir` in SuperCollider to see where this file is located.
  - On OS X it is usually in `~/Library/Application Support/SuperCollider`.

###install VTM on a raspberry pi

* first install supercollider from <https://github.com/redFrik/supercolliderStandaloneRPI2>
* then start sclang and do `Quarks.install("UnitTesting")`
* `git clone https://github.com/blacksound/VTM.git`
* Add the VTM folder path to under `- includePaths` in the `sclang_conf.yaml` file.
  - Run `Platform.userAppSupportDir` in SuperCollider to see where this file is located.
  - for supercolliderStandaloneRPI2: `nano ~/supercolliderStandaloneRPI2/sclang.yaml` and add `- /home/pi/VTM/Classes` under includePaths


#general raspberry pi instructions

###install jessie on raspberry pi

* sudo raspi-config - go to advanced options and select update
* enable VNC (in raspi-config)
* set GPU mem to more than default

###usb problem solving:

* ls /dev/
  - sudo mkdir /media/usb
  - sudo mount -t vfat -o uid=pi,gid=pi /dev/sda1 /media/usb
  - ls /media/usb


### shotdown.py for raspberry pi

* use the following script
#!/bin/python
import RPi.GPIO as GPIO
import os
pin= 3
GPIO.setmode(GPIO.BCM)
GPIO.setup(pin, GPIO.IN)
try:
	GPIO.wait_for_edge(pin, GPIO.FALLING)
	os.system("sudo halt -p")
except:
	pass
GPIO.cleanup()

* edit crontab
  - crontab -e
  - #and add the following…
  - @reboot python /home/pi/shutdown.py
