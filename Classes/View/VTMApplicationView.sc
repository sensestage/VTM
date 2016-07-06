VTMApplicationView : VTMView {
	var application;
	var showContentButtons, shownInContentView;
	var stageView, definitionsView, menuView, contextView, appNameView;
	var sceneOwnerView, moduleHostView, hardwareSetupView, networkView;

	*new{arg parent, bounds, application, description, definition;
		^super.new(parent, bounds, description, definition).initApplicationView(application);
	}

	initApplicationView{arg application_;
		application = application_;
		//make views
		appNameView = View()
		.layout_(HLayout(StaticText().string_("aaa")).spacing_(3).margins_(3))
		.background_(Color.cyan);

		menuView = View()
		.layout_(HLayout(StaticText().string_("menuView")).spacing_(3).margins_(3))
		.background_(Color(0.0, 0.1, 1.0));

		stageView = View()
		.layout_(HLayout(StaticText().string_("stageView")).spacing_(3).margins_(3))
		.background_(Color(0.0, 1.0, 0.2));

		contextView = View()
		.background_(Color(0.0, 0.1, 1.0));

		definitionsView = View()
		.layout_(HLayout(StaticText().string_("definitionsView")).spacing_(3).margins_(3))
		.background_(Color(0.5, 0.5, 0.1));

		sceneOwnerView = View()
		.background_(Color(1.0, 0.0, 0.0));

		moduleHostView = View()
		.background_(Color(0.0, 0.0, 1.0));

		hardwareSetupView = View()
		.background_(Color(0.1, 0.1, 0.7));

		networkView = View()
		.background_(Color(0.0, 0.0, 0.3));

		sceneOwnerView.layout_(
			HLayout(
				TreeView()
				.columns_(["SCENES"])
				.minHeight_(150)
				.fixedWidth_(150)
			).spacing_(3).margins_(3)
		);
		moduleHostView.layout_(
			HLayout(
				TreeView()
				.columns_(["MODULES"])
				.minHeight_(150)
				.fixedWidth_(150)
			).spacing_(3).margins_(3)
		);
		hardwareSetupView.layout_(
			HLayout(
				TreeView()
				.columns_(["HARDWARE"])
				.minHeight_(150)
				.fixedWidth_(150)
			).spacing_(3).margins_(3)
		);
		networkView.layout_(
			HLayout(
				TreeView()
				.columns_(["NETWORK"])
				.minHeight_(150)
				.fixedWidth_(150)
			).spacing_(3).margins_(3)
		);

		contextView.layout_(
			VLayout(
				[sceneOwnerView, align: \topLeft],
				[moduleHostView, align: \topLeft],
				[hardwareSetupView, align: \topLeft],
				[networkView, align: \topLeft]
			).spacing_(3).margins_(3)
		);

		this.layout_(
			VLayout(
				HLayout(//header view
					[appNameView, align: \topLeft],
					[menuView, align: \topLeft]
				),
				HLayout(
					[contextView.maxWidth_(155), align: \leftTop],
					[stageView, align: \center],
					[definitionsView.fixedWidth_(150), align: \rightTop]
				)
			).spacing_(3).margins_(3)
		);
		this.layout.spacing_(3).margins_(3);

	}

	showInStageView{arg val;

	}
}
