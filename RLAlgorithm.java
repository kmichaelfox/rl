package com.kmichaelfox.agents.rl;

import ch.idsia.benchmark.mario.environments.Environment;

public interface RLAlgorithm {
	void execute(float a, float g, float e);
	void useAlternateReward(boolean b);
}
