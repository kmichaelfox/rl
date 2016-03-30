This project is a package for the MarioAI simulation project (version 0.1.9), where the source code of this package should be imported to the MarioAI project in the package com.kmichaelfox.rl

All of the paramater varying trials can be run and written to file by executing:

	com.kmichaelfox.rl.ParameterTrials

The alternate reward function comparison trials can be run and written file by execting:
	
	com.kmichaelfox.rl.Main

Creation of a learning agent requires four arguments:

	RLAgent(float alpha, float gamma, float epsilon, LearningType type)

where, alpha = [0, 1], gamma = [0, 1], epsilon = [0, 1], and type is one of two enumerated values: LearningType.__QLEARNING__ or LearningType.__SARSA__

Based on the type provided, a separate object corresponding to the appropriate RL algorithm is created and attached to the _RLAgent_ object. These are the _QLearning_ and _SARSA_ classes. Each has its own self contained logic, though the classes are identical in most ways. The _getAction()_ method that is guaranteed by _RLAgent_'s base class, _BasicMarioAIAgent_, then executes the logic contained within these algorithm objects.

_DataWriter_ is a utility class that wraps file I/O functions. The class _EnvironmentHistory_ uses the built-in MarioAI _EnvironmentInfo_ class pipe simulation environment details into a history file using the _DataWriter_ class. Alternatively, _EnvironmentInfo_ can be given a string on each step of a simulation to record a raw string representing the simulation state. The client code must format the relevant information into the string before providing it to the logging class.