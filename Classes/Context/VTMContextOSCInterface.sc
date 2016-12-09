VTMContextOSCInterface {
	var context;
	var responders;

	*new{arg context;
		^super.new.init(context);
	}

	init{arg context_;
		context = context_;
		responders = IdentityDictionary.new;

		//build responders
		this.makeCommandResponders;
	}

	makeCommandResponders{
		context.class.commandFunctions.keysValuesDo({arg cmdKey, cmdFunc;
			var responderFunc, lastCmdChar, responderPath;
			lastCmdChar = cmdKey.asString.last;
			responderPath = "%%%".format(
				context.fullPath,
				context.class.contextCommandSeparator,
				cmdKey
			).asSymbol;
			switch(lastCmdChar,
				$!, {
					responderFunc = {arg msg, time, addr, port;
						cmdFunc.value(context);
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
								queryPath.asSymbol, cmdFunc.value(context));
						}, {
							"Context command '%' OSC missing query addr data".format().warn
						});
					};
				}
			);
			responders.put(
				cmdKey,
				OSCFunc(responderFunc, responderPath);
			);
		});
	}

	enable{
		responders.do(_.enable);
	}

	disable{
		responders.do(_.disable);
	}

	free{
		responders.do(_.free);
		context = nil;
		responders = nil;
	}
}
