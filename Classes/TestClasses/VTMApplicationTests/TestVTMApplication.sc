/*
Test setup with three applications running on the same computer.
*/
TestVTMApplication : UnitTest {

	test_StartApplication{}

	test_InitModuleHost{}

	test_InitSceneOwner{}

	test_InitHardwareSetup{}

	test_FilePaths{
		//Project paths, global paths, and project paths
	}

	test_RegisterNetworkApplicationsOnStartup{
		var result, aaa, bbb, ccc;
		var cond = Condition.new;
		aaa = VTMApplication.new('aaa', onRunning: {cond.unhang;});
		0.5.wait;
		bbb = VTMApplication.new('bbb', onRunning: {cond.unhang;});
		0.5.wait;
		ccc = VTMApplication.new('ccc', onRunning: {cond.unhang;});
		0.5.wait;
		//The application should now have registered eachother as application proxies.
		result = nil;
		result = result.add(aaa.network.applicationProxies.collect(_.name).includesAll([\bbb, \ccc]));
		result = result.add(bbb.network.applicationProxies.collect(_.name).includesAll([\aaa, \ccc]));
		result = result.add(ccc.network.applicationProxies.collect(_.name).includesAll([\bbb, \aaa]));
		// "aaa: %".format(aaa.network.applicationProxies.collect(_.name)).postln;
		// "bbb: %".format(bbb.network.applicationProxies.collect(_.name)).postln;
		// "ccc: %".format(ccc.network.applicationProxies.collect(_.name)).postln;
		this.assert(
			result.every({arg it; it;}),
			"Applications registered eachother correctly"
		);

		aaa.quit;
		0.5.wait;

		//app 'bbb' and 'ccc' should be notified upon 'aaa' quit
		result = nil;
		result = result.add( bbb.network.applicationProxies.collect(_.name).matchItem(\aaa).not );
		result = result.add( ccc.network.applicationProxies.collect(_.name).matchItem(\aaa).not );
		this.assert(
			result.every({arg item; item}),
			"Application 'bbb' and 'ccc' got notified of 'aaa' quit and removed its ApplicationProxy for it."
		);

		bbb.quit;
		ccc.quit;
	}

	test_LoadApplication{}

	test_StartAppFromFolder{
		//App is defined in the folder aaa.
	}

}
