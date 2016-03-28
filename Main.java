package com.kmichaelfox.agents.rl;

import java.util.ArrayList;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import com.kmichaelfox.agents.es.MLPAgent;

public class Main {
	public static void main(String[] args) {
//		RLAgent a = new RLAgent(0.1f, 0.2f, 0.3f, LearningType.SARSA);
//		a.learn();
//		
//		a = new RLAgent(0.6f, 0.7f, 0.8f, LearningType.QLEARNING);
//		a.learn();
		
		String argsString = "-vis off";
		MarioAIOptions marioAIOptions = new MarioAIOptions(argsString);
	    final Agent qAgent = new RLAgent(0.6f, 0.8f, 0.1f, LearningType.QLEARNING);
	    marioAIOptions.setAgent(qAgent);
	    //marioAIOptions.setExitProgramWhenFinished(true);
	    BasicTask task = new BasicTask(marioAIOptions);
	    
	    // learning loop
	    int iterations = 1000;
	    ArrayList<Float> fitnesses = new ArrayList<Float>();
	    for (int i = 0; i < iterations; i++) {
	    	task.runSingleEpisode(1);
	    	System.out.println(i + " " + task.getEvaluationInfo().computeBasicFitness());
	    }
	    fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
	    System.out.println("learning complete");
	    // performance test run
	    marioAIOptions.setVisualization(true);
	    marioAIOptions.setFPS(30);
	    task.runSingleEpisode(1);
	    
	    
	    
	    
	    argsString = "-vis off";
	    marioAIOptions = new MarioAIOptions(argsString);
	    final Agent sarsaAgent = new RLAgent(0.6f, 0.8f, 0.1f, LearningType.SARSA);
	    marioAIOptions.setAgent(sarsaAgent);
	    //marioAIOptions.setExitProgramWhenFinished(true);
	    task = new BasicTask(marioAIOptions);
	    
	    // learning loop
	    for (int i = 0; i < iterations; i++) {
	    	task.runSingleEpisode(1);
	    	System.out.println(i + " " + task.getEvaluationInfo().computeBasicFitness());
	    }
	    fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
	    System.out.println("learning complete");
	    // performance test run
	    marioAIOptions.setVisualization(true);
	    marioAIOptions.setFPS(30);
	    task.runSingleEpisode(1);
	    
	    
	    System.out.println("\n\n\n\n");
	    System.out.println("QLearning Fitness: "+fitnesses.get(0));
	    System.out.println("SARSA Fitness: "+fitnesses.get(1));
	    
	    System.exit(0);
	}
}
