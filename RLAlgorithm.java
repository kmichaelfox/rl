package com.kmichaelfox.agents.rl;

import ch.idsia.benchmark.mario.environments.Environment;

public interface RLAlgorithm {
	void execute();
	void useAlternateReward(boolean b);
}
