VTMValueParameterView : VTMParameterView {
	var <value;

	*new{arg parent, bounds, parameter, declaration, definition;
		^super.new(parent, bounds, parameter, declaration, definition).initValueParameterView;
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
