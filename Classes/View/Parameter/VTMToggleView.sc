VTMToggleView : VTMValueParameterView {
	var buttonView;

	*new{arg parent, bounds, parameter, declaration, definition;
		^super.new(parent, bounds, parameter, declaration, definition).initToggleView;
	}

	initToggleView {
		buttonView = Button(this, Rect(this.bounds.width - 30, 0, 30, this.class.unitHeight).insetBy(2,2))
		.states_([
					["", Color.black, Color.white.alpha_(1.0)],
					["X", Color.black, Color.white.alpha_(1.0)] ])
		.font_(Font("BlairMdITC TT", 16))
		.action_({|butt|
			parameter.valueAction_(butt.value.booleanValue, false).changed(\value, buttonView);
		});

		//this.prAddAltClickInterceptor(buttonView);//FIXME: this doesn;t work for this ui typee
		//Had to resort to this strange hack..
		buttonView.addAction(
			{arg v,x,y,mod;
				var result = false;
				if(mod != 524288, {
					{ buttonView.valueAction_(v.value.booleanValue.not.asInteger);}.defer;
					result = true;
				});
				result;
			},
			\mouseDownAction
		);
		this.refresh;
	}


	refresh{
			{
					buttonView.value_(parameter.value.asInteger);
			}.defer;
	}
}
