/*
Test setup with three applications running on the same computer.
*/
TestVTMApplication : TestVTMContext {

	test_StartApplication{}

	test_InitModuleHost{}

	test_InitSceneOwner{}

	test_InitHardwareSetup{}

	test_FilePaths{
		//Project paths, global paths, and project paths
	}

	test_RegisterNetworkApplicationsOnStartup{
		var result, aaa, bbb, ccc;
		var cond = false;
		aaa = VTMApplication.new('aaa', onRunning: {cond = true;});
		this.wait(cond, maxTime: 1.0);
		cond = false;
		bbb = VTMApplication.new('bbb', onRunning: {cond = true;});
		this.wait(cond, maxTime: 1.0);
		cond = false;
		ccc = VTMApplication.new('ccc', onRunning: {cond = true;});
		this.wait(cond, maxTime: 1.0);
		cond = false;
		//The application should now have registered eachother as application proxies.
		result = nil;
		result = result.add(aaa.network.applicationProxies.collect(_.name).includesAll([\bbb, \ccc]));
		result = result.add(bbb.network.applicationProxies.collect(_.name).includesAll([\aaa, \ccc]));
		result = result.add(ccc.network.applicationProxies.collect(_.name).includesAll([\bbb, \aaa]));
		cond = {
			[
				aaa.network.applicationProxies.collect(_.name).includesAll([\bbb, \ccc]),
				bbb.network.applicationProxies.collect(_.name).includesAll([\aaa, \ccc]),
				ccc.network.applicationProxies.collect(_.name).includesAll([\bbb, \aaa])
			].every({arg it; it;});
		};
		this.wait(
			cond,
			"Applications did not register eachother correctly",
			1.0
		);

		aaa.quit;
		cond = {
			[
				bbb.network.applicationProxies.collect(_.name).matchItem(\aaa).not,
				ccc.network.applicationProxies.collect(_.name).matchItem(\aaa).not
			].every({arg item; item});
		};
		this.wait(
			cond,
			"Application 'bbb' and 'ccc' did get got notified of 'aaa' quit and removed its ApplicationProxy for it.",
			maxTime: 1.0
		);

		bbb.quit;
		ccc.quit;
	}

	test_LoadApplication{}

	test_StartAppFromFolder{
		//App is defined in the folder aaa.
	}

}
