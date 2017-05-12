VTMValueParameterView : VTMParameterView {
	var <value;

	*new{arg parent, bounds, parameter, definition, attributes;
		^super.new(parent, bounds, parameter, definition, attributes).initValueParameterView;
	}

	initValueParameterView{
	}

	update{arg theChanged, whatChanged, whoChangedIt, toValue;
		if(theChanged === parameter, {
			switch(whatChanged,
				\value, { this.refresh; },
			{//default case
				super.update(theChanged, whatChanged, whoChangedIt, toValue);
			});
		});
	}
}
