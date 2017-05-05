//All classes that uses objects of this class must define a .makeOSCAPI
//classmethod returning getter and setter methods and functions for those.
//In order to define the OSC path the user class needs to define:
//- .path method.
//- .name method
//- .leadingSeparator
//- *makeOSCAPI(obj)
VTMOSCInterface {
	var model;
	var responderDict;
	var <enabled = false;

	*new{arg model;
		if(model.respondsTo(\fullPath).not, {
			NotYetImplementedError(
				"% has not implemented 'fullPath' method yet!".format(model.class)).throw;
		});

		^super.new.init(model);
	}

	init{arg model_;
		model = model_;
	}

	*prMakeResponders{arg model;
		var result = IdentityDictionary.new;
		/*
		model.class.makeOSCAPI(model).keysValuesDo({arg cmdKey, cmdFunc;
		var responderFunc, lastCmdChar, responderPath;
		lastCmdChar = cmdKey.asString.last;
		responderPath = model.fullPath;
		switch(lastCmdChar,
		$!, {
		responderFunc = {arg msg, time, addr, port;
		cmdFunc.value(model);
		};
		},
		$?, {
		responderFunc = {arg msg, time, addr, port;
		var queryHost, queryPath, queryPort;
		if(msg.size == 4, {
		var replyData;
		queryHost = msg[1].asString;
		queryPort = msg[2];
		queryPath = msg[3];
		replyData = cmdFunc.value(model);
		if(replyData.notNil, {
		if(replyData.isArray, {
		NetAddr(queryHost, queryPort).sendMsg(
		queryPath.asSymbol,
		*replyData
		);
		}, {
		NetAddr(queryHost, queryPort).sendMsg(
		queryPath.asSymbol,
		replyData
		);
		});
		});
		}, {
		"% command '%' OSC missing query addr data".format(
		model.class,
		responderPath
		).warn
		});
		};
		},
		//the default case is a setter method
		{
		responderFunc = {arg msg, time, addr, port;
		cmdFunc.value(msg[1..]);
		};
		}
		);
		result.put(
		cmdKey,
		OSCFunc(responderFunc, responderPath);
		);

		});
		*/
		result.put(\setters, this.prMakeSetterResponders(model));
		result.put(\queries, this.prMakeQueryResponders(model));
		result.put(\commands, this.prMakeCommandResponders(model));
		^result;
	}

	*prMakeSetterResponders{arg model;
		var result = IdentityDictionary.new;
		model.class.attributeKeys.do({arg attributeKey;
			//TODO: maybe move attribute separator somewhere potentionally more DRY?
			var path = (model.fullPath ++ '/' ++ attributeKey).asSymbol;
			result.put(
				path,
				OSCFunc({arg msg, time, resp, port;
					model.perform(attributeKey.asSetter, VTMJSON.parseAttributesString(msg[1]));
				}, path)
			);
		});
		^result;
	}

	*prMakeQueryResponders{arg model;
		var result = IdentityDictionary.new;
		^result;
	}

	*prMakeCommandResponders{arg model;
		var result = IdentityDictionary.new;
		^result;
	}

	enable{
		if(responderDict.isNil, {
			responderDict = this.class.prMakeResponders(model);
		});
		enabled = true;
	}

	disable{
		if(responderDict.notNil, {
			responderDict.keysValuesDo({arg respTypeKey, typeRespDict;
				typeRespDict.keys.do({arg k;
					typeRespDict.removeAt(k).free;
				});
			});
			responderDict = nil;
		});
		enabled = false;
	}

	free{
		this.disable;
		model = nil;
	}
}
