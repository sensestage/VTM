# VTM
VTM - system for Verdensteatret

###requirements

* UnitTesting quark
* sc3-plugins

###install VTM on a raspberry pi

* first install supercollider from <https://github.com/redFrik/supercolliderStandaloneRPI2>
* then start sclang and do `Quarks.install("UnitTesting")`
* `git clone https://github.com/blacksound/VTM.git`
* `mkdir ~/supercolliderStandaloneRPI2/share/user/Extensions`
* `ln -s ~/VTM/Classes/ ~/supercolliderStandaloneRPI2/share/user/Extensions/vtmclasses`
