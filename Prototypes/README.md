#Prototypes
A prototype is a form of interface. It is in some languages called a 'mixin'.

Module definitions can declare a set of prototypes which adds functionality to the module.

An example is 'audible' where the module definitions that declare this prototype will get methods related to playing, routing, muting, and setting volume added to its interface. Support for insert audio effect, and audio sends is also added.

This way one can semantically state that a module 'is' audible, without having to subclass it.
