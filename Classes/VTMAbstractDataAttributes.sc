VTMAbstractDataAttributes : VTMOrderedIdentityDictionary {

	*newFrom{arg what;
		var result;
		if(what.every(_.isKindOf(Association)), {
			var d;
			what.do({arg item;
				d = d.addAll([item.key, item.value]);
			});
			^super.newFrom(d);
		});
		^super.newFrom(what);
	}

	*readFromFile{arg pathName;
		^this.readArchive(pathName);
	}

	writeToFile{arg pathName, overwrite = false;
		if(File.exists(pathName), {
			if(overwrite == true, {
				this.prWriteFile(pathName);
			});
		}, {
			this.prWriteFile(pathName);
		});
	}

	prWriteFile{arg pathName;
		this.writeArchive(pathName);
	}


}