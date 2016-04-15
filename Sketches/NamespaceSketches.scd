VTMNamespaceElement.namespace

(
~network = VTMNamespaceElement.new(nil, 'network', 'a network object');
~node = VTMNamespaceElement.new(~network, 'bbb', 'a node object');
~moduleHost = VTMNamespaceElement.new(~node, 'module', 'a module host object');
~sceneOwner = VTMNamespaceElement.new(~node, 'scene', 'a scene owner object');
~hardwareSetup = VTMNamespaceElement.new(~node, 'hardware', 'a hardware setup object');
)

~node.children.keys
~hardwareSetup.parent.key

~network.find('bbb')

~toglyd = VTMNamespaceElement.new(nil, 'num1', 99);

~freq = VTMNamespaceElement.new(~myModule1, 'freq', 440.0);


~myModule1.parent;
~myParam1.parent.obj
'network/aaa/hardware/US2400/page/A/fader/2'
'/aaa/hardware/US2400/page.A/fader.2'

['/bbb/hardware/US24000/page.A:mapToScene', '/aaa/scene/togtur']

['/bbb/hardware/US24000/page.A:mapToScene<>', '/aaa/scene/togtur']


(
~system = (
	network: (
		aaa: (
			module: (),
			owner: (),
			hardware: (
				AAA: (name: \MOTU_UltraLite_AVB, inputs: 32, outputs: 32),
				US2400: (
					page: (
						A: (fader: 0.0 ! 24, button: (A: 0 ! 24, B: 0 ! 24, C: 0 ! 24)),
						B: (fader: 0.0 ! 24, button: (A: 0 ! 24, B: 0 ! 24, C: 0 ! 24)),
						C: (fader: 0.0 ! 24, button: (A: 0 ! 24, B: 0 ! 24, C: 0 ! 24)),
						D: (fader: 0.0 ! 24, button: (A: 0 ! 24, B: 0 ! 24, C: 0 ! 24))
					)
				)
			)
		),
		bbb: (
			module: (),
			owner: (),
			hardware: (
				BBB: (name: \MOTU_UltraLite_AVB, inputs: 32, outputs: 32),
				US2400: (
					name: \US2400,
					page: (
						A: (fader: 0.0 ! 24, button: (A: 0 ! 24, B: 0 ! 24, C: 0 ! 24)),
						B: (fader: 0.0 ! 24, button: (A: 0 ! 24, B: 0 ! 24, C: 0 ! 24)),
						C: (fader: 0.0 ! 24, button: (A: 0 ! 24, B: 0 ! 24, C: 0 ! 24)),
						D: (fader: 0.0 ! 24, button: (A: 0 ! 24, B: 0 ! 24, C: 0 ! 24))
					)
				)
			)
		),
		lydserver: (
			module: (),
			owner: (),
			hardware: [
				ZZZ: (name: \MOTU_16io, inputs: 32, outputs: 32),
				XXX: (name: \MOTU_24Ao, inputs: 32, outputs: 32),
				QQQ: (name: \MOTU_24Ao, inputs: 32, outputs: 32),
				YYY: (name: \MOTU_24Ao, inputs: 32, outputs: 32)
			]
		),
		flaate: (
			module: (
				tallerken: (
					\1: (bipolarSpeed: 0, stop: {}, hardware: 'hardware/miniMonster.1', channel: 1),
					\2: (bipolarSpeed: 0, stop: {}, hardware: 'hardware/miniMonster.1', channel: 2),
					\3: (bipolarSpeed: 0, stop: {}, hardware: 'hardware/miniMonster.2', channel: 1),
				),
				hvitFugl: (hardware: 'hardware/hvitFugl')
			),
			owner: (),
			hardware: (
				torgrimFader: (
					\1: (name: \BCF2000),
					\2: (name: \BCF2000)
				),
				miniMonster: (
					\1: (path: '/dev/tty.81818', name: 'miniMonster'),
					\2: (path: '/dev/tty.9191919', name: 'miniMonster')
				),
				hvitFugl: (path: '/dev/tty.71717', name: 'hvitFugl')
			)
		),
		mekanikk: (
			module: (
				pinne: (
					\1: (
						leftMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rightMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rotationMotor: (speed: 0, stop: {}, gotoPosition: {}),
					),
					\2: (
						leftMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rightMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rotationMotor: (speed: 0, stop: {}, gotoPosition: {}),
					),
					\3: (
						leftMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rightMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rotationMotor: (speed: 0, stop: {}, gotoPosition: {}),
					),
					\4: (
						leftMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rightMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rotationMotor: (speed: 0, stop: {}, gotoPosition: {}),
					),
					\5: (
						leftMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rightMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rotationMotor: (speed: 0, stop: {}, gotoPosition: {}),
					),
					\6: (
						leftMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rightMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rotationMotor: (speed: 0, stop: {}, gotoPosition: {}),
					),
					\7: (
						leftMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rightMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rotationMotor: (speed: 0, stop: {}, gotoPosition: {}),
					),
					\8: (
						leftMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rightMotor: (speed: 0, stop: {}, gotoPosition: {}),
						rotationMotor: (speed: 0, stop: {}, gotoPosition: {}),
					),
				),
				lokomotiv: (
					\1: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\2: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\3: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\4: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\5: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\6: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\7: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\8: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\9: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\10: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\11: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\12: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\13: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {}),
					\14: (speed: 0, position: 0, stop: {}, startTracking: {}, stopTracking: {})
				),
				kopp: (
					\1: (speed: 0, position: 0, stop: {}),
					\2: (speed: 0, position: 0, stop: {}),
					\3: (speed: 0, position: 0, stop: {}),
					\4: (speed: 0, position: 0, stop: {}),
					\5: (speed: 0, position: 0, stop: {}),
					\6: (speed: 0, position: 0, stop: {})
				),
				krans: (
					\1: (speed: 0, stop: {}, hardware: 'hardware/KransAPI/channel.1'),
					\2: (speed: 0, stop: {}, hardware: 'hardware/KransAPI/channel.2'),
					\3: (speed: 0, stop: {}, hardware: 'hardware/KransAPI/channel.3')
				)
			),
			owner: (),
			hardware: (
				PinneAPI: (
					\1: (path: '/dev/tty.GGGG'),
					\2: (path: '/dev/tty.IIII'),
					\3: (path: '/dev/tty.JJJJ'),
					\4: (path: '/dev/tty.KKKK'),
					\5: (path: '/dev/tty.LLLL'),
					\6: (path: '/dev/tty.MMMM'),
					\7: (path: '/dev/tty.NNNN'),
					\8: (path: '/dev/tty.OOOO')
				),
				KoppAPI: (
					\1: (path: '/dev/tty.AAAA'),
					\2: (path: '/dev/tty.BBBB'),
					\3: (path: '/dev/tty.CCCC'),
					\4: (path: '/dev/tty.DDDD'),
					\5: (path: '/dev/tty.EEEE'),
					\6: (path: '/dev/tty.FFFF')
				),
				KransAPI: (
					path: '/dev/tty.QQQQ'
				)
			)
		)
	)
)

)