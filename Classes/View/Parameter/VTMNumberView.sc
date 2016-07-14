VTMNumberView : VTMScalarParameterView {
	var numberView;
	var round;
	var <decimals;

	*new{arg parent, bounds, parameter, declaration, definition;
		^super.new(parent, bounds, parameter, declaration, definition).initNumberView;
	}

	initNumberView {

		numberView = NumberBox(this, this.bounds)
		.action_({ |num|
			parameter.valueAction_(num.value, false).changed(\value, numberView);
		})
		.font_(this.class.font)
		.background_(this.class.elementColor)
		.focusColor_(this.class.elementColor)
		.normalColor_(this.class.stringColor)
		.align_(\right);

		this.prAddAltClickInterceptor(numberView);//all topmost views need to set this

		if(parameter.type == \integer, {
			this.decimals_(0);
			numberView.step_(1);
			numberView.scroll_step_(1);
		}, {
			this.decimals_(declaration.atFail(\decimals, {2}));
			numberView.step_(0.1);
			numberView.scroll_step_(0.01);
		});
		this.refresh;
	}

	decimals_{arg val;
		decimals = val;
		round = (10 ** decimals).reciprocal;
		numberView.decimals_(decimals);
		this.refresh;
	}

	refresh{
			{
					numberView.value_(parameter.value.round(round));
			}.defer;
	}
}
