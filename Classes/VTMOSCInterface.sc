VTMOSCInterface {
	var model;
	var responders;
	var <enabled = false;

	*new{arg model;
		^super.new.init(model);
	}

	init{arg model_;
		model = model_;
	}

	*prMakeResponders{arg model;
		var result = IdentityDictionary.new;
		model.class.makeOSCAPI(model).keysValuesDo({arg cmdKey, cmdFunc;
			var responderFunc, lastCmdChar, responderPath;
			lastCmdChar = cmdKey.asString.last;
			responderPath = "%%%".format(
				model.fullPath,
				VTM.commandSeparator,
				cmdKey
			).asSymbol;
			switch(lastCmdChar,
				$!, {
					responderFunc = {arg msg, time, addr, port;
						cmdFunc.value(model);
					};
				},
				$?, {
					responderFunc = {arg msg, time, addr, port;
						var queryAddr, queryPath, queryPort;
						if(msg.size == 4, {
							queryAddr = msg[1];
							queryPort = msg[2];
							queryPath = msg[3];
							NetAddr(queryAddr, queryPort).sendMsg(
								queryPath.asSymbol, cmdFunc.value(model)
							);
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
		^result;
	}

	enable{
		if(responders.isNil, {
			responders = this.class.prMakeResponders(model);
		});
		enabled = true;
	}

	disable{
		if(responders.notNil, {
			responders.do({arg resp;
				resp.free;
			});
			responders = nil;
		});
		enabled = false;
	}

	free{
		this.disable;
		model = nil;
	}
}
