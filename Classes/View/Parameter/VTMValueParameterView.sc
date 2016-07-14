VTMValueParameterView : VTMParameterView {
	var <value;

	*new{arg parent, bounds, parameter, description, definition;
		^super.new(parent, bounds, parameter, description, definition).initValueParameterView;
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
