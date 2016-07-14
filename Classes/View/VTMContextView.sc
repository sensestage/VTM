VTMContextView : VTMView {
	var <context;
	var labelView;
	var contentView;
	var headerView;

	*new{arg parent, bounds, context, declaration, definition;
		^super.new(parent, bounds, declaration, definition).initContextView(context);
	}

	initContextView{arg context_;
		context = context_;
		context.addDependant(this);

		this.prInitLabelView;
		contentView = View(this).layout_(HLayout(StaticText().string_("context content view")));
		headerView = View(this);
		this.layout_(
			VLayout(
				HLayout(
					[labelView.maxSize_(Size(200,25)), align: \topLeft],
					[headerView.maxHeight_(25), align: \top]
				),
				contentView
			)
		);
		this.layout.spacing_(3).margins_(3);
		/*this.refreshLabel;*/
		this.refresh;
	}

	prInitLabelView{
		labelView = StaticText().string_(context.name);
	}

	free{
		context.removeDependant(this);
	}

	refreshLabel{
		{
			labelView.string_(context.name);
			labelView.toolTip = context.path;
		}.defer;
	}

	refreshContextView{arg what = \all;
		switch(what,
			\scenes, {
				this.refreshSceneList();
			},
			\modules, {
				this.refreshModulesList();
			},
			\hardware, {
				this.refreshHardwareList();
			},
			\network, {
				this.refreshNetworkList();
			},
			{
				this.refreshSceneList();
				this.refreshModulesList();
				this.refreshHardwareList();
				this.refreshNetworkList();
			}
		)
	}

	refreshSceneList{}
	refreshModulesList{}
	refreshHardwareList{}
	refreshNetworkList{}

	//pull style update
	update{arg theChanged, whatChanged, whoChangedIt, toValue;
		//"Dependant update: % % % %".format(theChanged, whatChanged, whoChangedIt, toValue).postln;
		if(theChanged === context, {//only update the view if the parameter changed
			switch(whatChanged,
				//\enabled, { this.enabled_(context.enabled); },
				\path, { this.refreshLabel; },
				\name, { this.refreshLabel; },
				\freed, { this.free; }
			);
			this.refresh;
		}, {
			super.update(theChanged, whatChanged, whoChangedIt, toValue);
		});
	}
}
