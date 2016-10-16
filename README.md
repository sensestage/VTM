# VTM
VTM - system for Verdensteatret

###requirements

* UnitTesting quark
* sc3-plugins

###Install VTM

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
