VTMSliderView : VTMScalarParameterView {
	var numberView;
	var sliderView;
	var round;
	var <decimals;

	*new{arg parent, bounds, parameter, description, definition;
		^super.new(parent, bounds, parameter, description, definition).initSliderView;
	}

	initSliderView {
		var sliderBounds;
		//using 1 units as default
		description.atFail(\units, {
			description.put(\units, 1);
		});
		units = description[\units].clip(1,2);
		this.bounds_(this.class.prCalculateSize(units).asRect);
		switch(units,
			1, { sliderBounds = this.bounds; },
			2, { sliderBounds = this.class.prCalculateSize(1).asRect.moveBy(0, this.class.unitHeight) }
		);

		sliderView = Slider.new(this, sliderBounds)
		.thumbSize_(3)
		.action_({arg slid;
			var newValue;
			newValue = spec.map(slid.value);
			parameter.valueAction_(newValue, false);
			parameter.changed(\value, slid);
		})
		.background_(this.class.elementColor);

		numberView = NumberBox(this, this.class.prCalculateSize(1).asRect.insetBy(1,1))
		.acceptsMouse_(false)
		.font_(this.class.font)
		.background_(Color.white.alpha_(0.0))
		//.focusColor_(this.class.elementColor)
		//.normalColor_(this.class.stringColor)
		.canFocus_(true)
		.align_(\right)
		.action_({arg v; parameter.valueAction_(v.value, false).changed(\value, v)});

		this.prAddAltClickInterceptor(sliderView);
		this.prAddAltClickInterceptor(numberView);

		if(parameter.type == \integer, {
			this.decimals_(0);
		}, {
			this.decimals_(description.atFail(\decimals, {2}));
		});
		this.refresh;
	}

	decimals_{arg val;
		decimals = val;
		round = (10 ** decimals).reciprocal;
		numberView.decimals_(decimals);
		this.refresh;
	}

	update{arg theChanged, whatChanged, whoChangedIt, toValue;
		super.update(theChanged, whatChanged, whoChangedIt, toValue);
		if(theChanged === parameter, {
			if(whatChanged == \value, {
				{
					if(whoChangedIt !== numberView, {
						numberView.value_(parameter.value.round(round));
					});
					if(whoChangedIt !== sliderView, {
						sliderView.value_(spec.unmap(parameter.value));
					});
				}.defer;
			});
		});
	}

	refresh{
			{
					numberView.value_(parameter.value.round(round));
					sliderView.value_(spec.unmap(parameter.value));
			}.defer;
	}
}
