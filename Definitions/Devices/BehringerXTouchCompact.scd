(
	prototypes: [\midiDevice],
	prepare: {arg module;
		module.envir.use{
			var mappings = [
				[
					'layer.A/fader.1' -> (number: 1),
					'layer.A/fader.2' -> (number: 2),
					'layer.A/fader.3' -> (number: 3),
					'layer.A/fader.4' -> (number: 4),
					'layer.A/fader.5' -> (number: 5),
					'layer.A/fader.6' -> (number: 6),
					'layer.A/fader.7' -> (number: 7),
					'layer.A/fader.8' -> (number: 8),
					'layer.A/fader.main' -> (number: 9),
					'layer.A/faderTouch.1' -> (number: 101),
					'layer.A/faderTouch.2' -> (number: 102),
					'layer.A/faderTouch.3' -> (number: 103),
					'layer.A/faderTouch.4' -> (number: 104),
					'layer.A/faderTouch.5' -> (number: 105),
					'layer.A/faderTouch.6' -> (number: 106),
					'layer.A/faderTouch.7' -> (number: 107),
					'layer.A/faderTouch.8' -> (number: 108),
					'layer.A/faderTouch.main' -> (number: 109),
					'layer.A/rotary.1' -> (number: 10),
					'layer.A/rotary.2' -> (number: 11),
					'layer.A/rotary.3' -> (number: 12),
					'layer.A/rotary.4' -> (number: 13),
					'layer.A/rotary.5' -> (number: 14),
					'layer.A/rotary.6' -> (number: 15),
					'layer.A/rotary.7' -> (number: 16),
					'layer.A/rotary.8' -> (number: 17),
					'layer.A/rotary.9' -> (number: 18),
					'layer.A/rotary.10' -> (number: 19),
					'layer.A/rotary.11' -> (number: 20),
					'layer.A/rotary.12' -> (number: 21),
					'layer.A/rotary.13' -> (number: 22),
					'layer.A/rotary.14' -> (number: 23),
					'layer.A/rotary.15' -> (number: 24),
					'layer.A/rotary.16' -> (number: 25),
					'layer.B/fader.1' -> (number: 28),
					'layer.B/fader.2' -> (number: 29),
					'layer.B/fader.3' -> (number: 30),
					'layer.B/fader.4' -> (number: 31),
					'layer.B/fader.5' -> (number: 32),
					'layer.B/fader.6' -> (number: 33),
					'layer.B/fader.7' -> (number: 34),
					'layer.B/fader.8' -> (number: 35),
					'layer.B/fader.main' -> (number: 36),
					'layer.B/faderTouch.1' -> (number: 111),
					'layer.B/faderTouch.2' -> (number: 112),
					'layer.B/faderTouch.3' -> (number: 113),
					'layer.B/faderTouch.4' -> (number: 114),
					'layer.B/faderTouch.5' -> (number: 115),
					'layer.B/faderTouch.6' -> (number: 116),
					'layer.B/faderTouch.7' -> (number: 117),
					'layer.B/faderTouch.8' -> (number: 118),
					'layer.B/faderTouch.main' -> (number: 119),
					'layer.B/rotary.1' -> (number: 37),
					'layer.B/rotary.2' -> (number: 38),
					'layer.B/rotary.3' -> (number: 39),
					'layer.B/rotary.4' -> (number: 40),
					'layer.B/rotary.5' -> (number: 41),
					'layer.B/rotary.6' -> (number: 42),
					'layer.B/rotary.7' -> (number: 43),
					'layer.B/rotary.8' -> (number: 44),
					'layer.B/rotary.9' -> (number: 45),
					'layer.B/rotary.10' -> (number: 46),
					'layer.B/rotary.11' -> (number: 47),
					'layer.B/rotary.12' -> (number: 48),
					'layer.B/rotary.13' -> (number: 49),
					'layer.B/rotary.14' -> (number: 50),
					'layer.B/rotary.15' -> (number: 51),
					'layer.B/rotary.16' -> (number: 52),
				].collect({arg item;
					item.key -> (item.value ++ (msgType: \control));
				}),
				[
					'layer.A/button.1/A' -> (number: 32),
					'layer.A/button.2/A' -> (number: 33),
					'layer.A/button.3/A' -> (number: 34),
					'layer.A/button.4/A' -> (number: 35),
					'layer.A/button.5/A' -> (number: 36),
					'layer.A/button.6/A' -> (number: 37),
					'layer.A/button.7/A' -> (number: 38),
					'layer.A/button.8/A' -> (number: 39),
					'layer.A/button.1/B' -> (number: 24),
					'layer.A/button.2/B' -> (number: 25),
					'layer.A/button.3/B' -> (number: 26),
					'layer.A/button.4/B' -> (number: 27),
					'layer.A/button.5/B' -> (number: 28),
					'layer.A/button.6/B' -> (number: 29),
					'layer.A/button.7/B' -> (number: 30),
					'layer.A/button.8/B' -> (number: 31),
					'layer.A/button.1/C' -> (number: 16),
					'layer.A/button.2/C' -> (number: 17),
					'layer.A/button.3/C' -> (number: 18),
					'layer.A/button.4/C' -> (number: 19),
					'layer.A/button.5/C' -> (number: 20),
					'layer.A/button.6/C' -> (number: 21),
					'layer.A/button.7/C' -> (number: 22),
					'layer.A/button.8/C' -> (number: 23),
					'layer.A/button.1/D' -> (number: 40),
					'layer.A/button.2/D' -> (number: 41),
					'layer.A/button.3/D' -> (number: 42),
					'layer.A/button.4/D' -> (number: 43),
					'layer.A/button.5/D' -> (number: 44),
					'layer.A/button.6/D' -> (number: 45),
					'layer.A/button.7/D' -> (number: 46),
					'layer.A/button.8/D' -> (number: 47),
					'layer.A/button.main' ->  (number: 48),
					'layer.A/button.rewind' ->  (number: 49),
					'layer.A/button.forward' ->  (number: 50),
					'layer.A/button.loop' -> (number: 51),
					'layer.A/button.rec' ->  (number: 52),
					'layer.A/button.stop' -> (number: 53),
					'layer.A/button.play' -> (number: 54),
					'layer.A/rotaryButton.1' -> (number: 0),
					'layer.A/rotaryButton.2' -> (number: 1),
					'layer.A/rotaryButton.3' -> (number: 2),
					'layer.A/rotaryButton.4' -> (number: 3),
					'layer.A/rotaryButton.5' -> (number: 4),
					'layer.A/rotaryButton.6' -> (number: 5),
					'layer.A/rotaryButton.7' -> (number: 6),
					'layer.A/rotaryButton.8' -> (number: 7),
					'layer.A/rotaryButton.9' -> (number: 8),
					'layer.A/rotaryButton.10' -> (number: 9),
					'layer.A/rotaryButton.11' -> (number: 10),
					'layer.A/rotaryButton.12' -> (number: 11),
					'layer.A/rotaryButton.13' -> (number: 12),
					'layer.A/rotaryButton.14' -> (number: 13),
					'layer.A/rotaryButton.15' -> (number: 14),
					'layer.A/rotaryButton.16' -> (number: 15),
					'layer.B/button.1/A' -> (number: 87),
					'layer.B/button.2/A' -> (number: 88),
					'layer.B/button.3/A' -> (number: 89),
					'layer.B/button.4/A' -> (number: 90),
					'layer.B/button.5/A' -> (number: 91),
					'layer.B/button.6/A' -> (number: 92),
					'layer.B/button.7/A' -> (number: 93),
					'layer.B/button.8/A' -> (number: 94),
					'layer.B/button.1/B' -> (number: 79),
					'layer.B/button.2/B' -> (number: 80),
					'layer.B/button.3/B' -> (number: 81),
					'layer.B/button.4/B' -> (number: 82),
					'layer.B/button.5/B' -> (number: 83),
					'layer.B/button.6/B' -> (number: 84),
					'layer.B/button.7/B' -> (number: 85),
					'layer.B/button.8/B' -> (number: 86),
					'layer.B/button.1/C' -> (number: 71),
					'layer.B/button.2/C' -> (number: 72),
					'layer.B/button.3/C' -> (number: 73),
					'layer.B/button.4/C' -> (number: 74),
					'layer.B/button.5/C' -> (number: 75),
					'layer.B/button.6/C' -> (number: 76),
					'layer.B/button.7/C' -> (number: 77),
					'layer.B/button.8/C' -> (number: 78),
					'layer.B/button.1/D' -> (number: 95),
					'layer.B/button.2/D' -> (number: 96),
					'layer.B/button.3/D' -> (number: 97),
					'layer.B/button.4/D' -> (number: 98),
					'layer.B/button.5/D' -> (number: 99),
					'layer.B/button.6/D' -> (number: 100),
					'layer.B/button.7/D' -> (number: 101),
					'layer.B/button.8/D' -> (number: 102),
					'layer.B/button.main' -> (number: 103),
					'layer.B/button.rewind' -> (number: 104),
					'layer.B/button.forward' -> (number: 105),
					'layer.B/button.loop' -> (number: 106),
					'layer.B/button.rec' -> (number: 107),
					'layer.B/button.stop' -> (number: 108),
					'layer.B/button.play' -> (number: 109),
					'layer.B/rotaryButton.1' -> (number: 55),
					'layer.B/rotaryButton.2' -> (number: 56),
					'layer.B/rotaryButton.3' -> (number: 57),
					'layer.B/rotaryButton.4' -> (number: 58),
					'layer.B/rotaryButton.5' -> (number: 59),
					'layer.B/rotaryButton.6' -> (number: 60),
					'layer.B/rotaryButton.7' -> (number: 61),
					'layer.B/rotaryButton.8' -> (number: 62),
					'layer.B/rotaryButton.9' -> (number: 63),
					'layer.B/rotaryButton.10' -> (number: 64),
					'layer.B/rotaryButton.11' -> (number: 65),
					'layer.B/rotaryButton.12' -> (number: 66),
					'layer.B/rotaryButton.13' -> (number: 67),
					'layer.B/rotaryButton.14' -> (number: 68),
					'layer.B/rotaryButton.15' -> (number: 69),
					'layer.B/rotaryButton.16' -> (number: 70)
				].collect({arg item;
					[
						"%/press".format(item.key).asSymbol -> (item.value ++ (msgType: \noteOn)),
						"%/release".format(item.key).asSymbol -> (item.value ++ (msgType: \noteOff))
					]
				})
			].flat;

			//~midiController.addComponent();
		};
	};
);