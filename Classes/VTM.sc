VTM{
	*local{
		^VTMLocalNetworkNode.singleton;
	}

	*sendMsg{arg hostname, port, path ...data;
		this.local.sendMsg(hostname, port, path, *data);
	}

	*sendLocalMsg{arg path ...data;
		this.sendMsg(
			NetAddr.localAddr.hostname, NetAddr.localAddr.port,
			path, *data
		);
	}

	*activate{arg val, discovery = false;
		this.local.activate(val, discovery);
	}

	*deactivate{
		this.local.deactivate;
	}

	*discover{
		this.local.discover;
	}
}
