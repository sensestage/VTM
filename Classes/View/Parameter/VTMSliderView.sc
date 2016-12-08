VTMSliderView : VTMNumberParameterView {
	var numberView;
	var sliderView;
	var round;
	var <decimals;

	*new{arg parent, bounds, parameter, definition, declaration;
		^super.new(parent, bounds, parameter, definition, declaration).initSliderView;
	}

	initSliderView {
		var sliderBounds;
		//using 1 units as default
		declaration.atFail(\units, {
			declaration.put(\units, 1);
		});
		units = declaration[\units].clip(1,2);
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

		// this.prAddAltClickInterceptor(sliderView);
		// this.prAddAltClickInterceptor(numberView);

		if(parameter.type == \integer, {
			this.decimals_(0);
		}, {
			this.decimals_(declaration.atFail(\decimals, {2}));
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
		if(theChanged === parameter, {
			switch(whatChanged,
				\value, {
					{
						if(whoChangedIt !== numberView, {
							numberView.value_(parameter.value.round(round));
						});
						if(whoChangedIt !== sliderView, {
							sliderView.value_(spec.unmap(parameter.value));
						});
					}.defer;
				},
				{
						super.update(theChanged, whatChanged, whoChangedIt, toValue);
				}
			);
		});
	}

	refresh{
			{
					numberView.value_(parameter.value.round(round));
					sliderView.value_(spec.unmap(parameter.value));
			}.defer;
	}
}
