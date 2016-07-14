VTMRemoteContextProxyImplementation : VTMContextProxyImplementation {
	var <targetAddr;
	var <targetPath;

	*new{arg context, declaration, definition;
		^super.new(context, declaration, definition).initRemoteContextProxyImplementation;
	}

	initRemoteContextProxyImplementation{
		if(declaration.notNil, {
			if(declaration.includesKey(\targetAddr), {
				targetAddr = declaration[\targetAddr];
			});
			if(declaration.includesKey(\targetPath), {
				targetPath = declaration[\targetPath];
			});
		});
		if(targetPath.isNil, {
			targetPath = "/%".format(context.name).asSymbol;
		});
	}

	sendMsg{arg subpath ...msg;
		targetAddr.sendMsg("%%".format(this.targetPath, subpath), *msg);
	}
}
