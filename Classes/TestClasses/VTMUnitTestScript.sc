VTMUnitTestScript : VTMUnitTest {

	var <>name, <>path;

	classvar <allScripts;
	classvar fileEnd = "_unittest.scd";
	classvar scriptDict;


	*new { |name, path|
		^super.new.init(name, path)
	}

	init { |argName, argPath|
		name = argName;
		path = argPath;
	}

	*initClass {
		scriptDict = ();
	}

	*runTest { | scriptName |
		var script;
		allScripts ?? { this.findTestScripts };
		script = VTMUnitTestScript(scriptName, scriptDict[scriptName.asSymbol]);
		if(script.isNil) { ("UnitTestScript: script not found: "+ scriptName ).warn } {
			script.runScript
		}
	}

	*runAll{
		allScripts ?? { this.findTestScripts; };
		allScripts.do({arg script;
			script.runScript;
		});
	}

	*findTestScripts {
		var classPaths;
		var func = { |path|
			var scriptPaths,fileNames, scriptNames;
			scriptPaths = pathMatch(path ++"/*" ++ fileEnd);
			scriptPaths = scriptPaths ++ pathMatch(path ++"/*/*" ++ fileEnd);
			scriptPaths = scriptPaths.as(Set).as(Array); // remove duplicates
			fileNames = scriptPaths.collect(_.basename);
			scriptNames = fileNames.collect { |x| x.replace(fileEnd, "").asSymbol };
			scriptNames.do { |name, i|
				var oldPath = scriptDict.at(name);
				if(oldPath.notNil and: { oldPath != scriptPaths[i] }) {
					Error(
						"duplicate script name:\n%\n%\n\npath:%\n\n"
						.format(scriptPaths[i], scriptDict[name], path)
					);
				};
				scriptDict.put(name, scriptPaths[i]);
				if(oldPath.notNil) { allScripts.add(this.new(name, scriptPaths[i])) };
			};
		};

		classPaths = Class.allClasses.collectAs({ |class| class.filenameSymbol.asString.dirname }, Set);
		allScripts = List.new;
		classPaths.do(func);

	}

	*findTestMethods {
		this.findTestScripts;
		^allScripts
	}

	runTestMethod { |testScript|

		testScript.runScript;

	}

	runScript {
		("RUNNING UNIT TEST SCRIPT" + name ++ " path:" ++ path ++ "\n\n").inform;
		this.class.forkIfNeeded {
			currentMethod = this;

			// a weakness with test scripts is that they don't
			// throw the unexpected errors to sclang, only as primitive errors,
			// so every time we make a test script we have to make sure that it compiles
			// by evaluating the whole file manually.
			if(VTMUnitTest.reportAllErrors, {
				var str;
				var file = File.open(path, "r");
				str = file.readAllString;
				file.close;
				try{
					path.load.value(this);
				} {|err|
					this.failed(currentMethod,
						"ERROR: during test: \n\t%".format(err.errorString)
					)
				};
			}, {
				path.load.value(this);
			});




			this.class.report;
			nil
		}
	}

	run {
		allScripts ?? { this.class.findTestScripts };
		Routine {
			allScripts.do { |testScript|
				this.runTestMethod(testScript)
			}
		}.play(AppClock);
	}

}