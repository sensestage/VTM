/*

Why do we need to separate the concept of Modules and NodeModules?
There is a need for creating modules on the fly. Without it having to be defined within a context.
I'm not sure if this will create an uneccesary level of indirection, but my experience from rehearsal situations tells me that it will be a good tool to have.

If we need a module ad-hoc, it will be a matter of defining it, regardless of it having to be running within a VTM system.
This makes prototyping on platforms and computer that may not have the full potential for a full fledged VTM system.

A module will be able to:
- Store presets
- Automate parameters (also in Parameter class)
- Subscribe/bind and map to other parameters.
- OSC support
- attach action to value changes
- report state changes to dependants (which in the case of being wrapped in a NodeModule object will be the ModuleHost.)
- Define parameter types, ranges, defaults, etc.
- Make GUIs on the spot.
- Wrap submodules.

What will be in the NodeModule class that is not in the Module class?

The separation idea is based on whether the module should understand what it means to be hosted on a node in VTM system.
One thought is to model the Module class in a way that it won't know anything about what being hosted by a node means.
I could start out with putting managing code into the ModuleHost class.
On the other hand, I really want the NodeModule to be a context, by inheritance.

And here is another problem: If a module should have parameter, it is the idea that the module is a context for its parameters. So, the module must be a context, but it should be somehow optional to run the module in a ModuleHost.
So, what does ModuleHost actually do? On emisunderstanding is that the ModuleHost is a NodeContext, while the Module, or NodeModule, is not. I.e. the module doesn't need to know about what parent it has, and having a parent must optional for modules.

One strategy for developing the module and parameter classes is to try to model them as standalone classes, outside the VTM paradigm. A module _can_ have a ModuleHost, but this is not mandatory. It is not even defined within the module class.
Same thing goes for Parameter objects. they can be a part of a Module, but they don't need to be. The Module will know about the concept of having Parameters, but a parameter only know about being a parameter.

So this leads me to nuance between the concept of a context and ownership.
It may very well be that my current implementation of Context is indeed a form of managing class. this may lead to a clearer separation of concerns between a context, a namespace, and managing/ownership for classes.

A ModuleHost manages Modules. A module manages parameters. A parameter manages parameter settings. A Node manages a ModuleHost, a SceneOwner, and a HardwareSetup.
A Network manages its Nodes.

While on the other hand, a Module is a context for (Sub)Modules and parameters.
The difference between a 'manager role', and 'being a context' is hard to spot.

What reasons do we have for having the module being a context?
What reasons do we have for having modules being aware of its parent (the ModuleHost, and its Node)?
  Network between modules is one thing. But one solution here is to leave inter-module and inter-scene attributes up to the Factory classes.
Making a module and a parameter as nerdy as focussed as possible is the goal. It is not the Context's resposibility to manage its parent concept.
they will however be linked in fate, i.e. if the parent context is freed, the child context should also be freed. But this again will be the responsibility of the parent; to free its children.

*/

/*
Side note: Should provide a way of generating parameters from internal SynthDef/Synth code. Providing ranges and other parameter specific code will be optional. The view element that will be used for this will be a textbox of decimal number, so that the concept of range etc. is left up to the user to control.
*/
