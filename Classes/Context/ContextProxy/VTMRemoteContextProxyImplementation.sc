VTMRemoteContextProxyImplementation : VTMContextProxyImplementation {
	var <targetAddr;
	var <targetPath;

	*new{arg context, description, definition;
		^super.new(context, description, definition).initRemoteContextProxyImplementation;
	}

	initRemoteContextProxyImplementation{
		if(description.notNil, {
			if(description.includesKey(\addr), {
				targetAddr = description[\addr];
			});
			if(description.includesKey(\path), {
				targetPath = description[\path];
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
