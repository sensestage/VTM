VTMRemoteContextProxyImplementation : VTMContextProxyImplementation {
	var <targetAddr;
	var <targetPath;

	*new{arg context, definition, attributes;
		^super.new(context, definition, attributes).initRemoteContextProxyImplementation;
	}

	initRemoteContextProxyImplementation{
		if(attributes.notNil, {
			if(attributes.includesKey(\targetAddr), {
				targetAddr = attributes[\targetAddr];
			});
			if(attributes.includesKey(\targetPath), {
				targetPath = attributes[\targetPath];
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
