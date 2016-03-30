package com.kmichaelfox.agents.rl;

import java.util.ArrayList;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import com.kmichaelfox.agents.es.MLPAgent;

public class Main {
	public static void main(String[] args) {
		int iterations = 1000;
		boolean useRewardForLog = false;
		float alpha = 0.15f;
		float gamma = 0.6f;
		float epsilon = 0.3f;
		
		//
		//	Q-Learning
		//
		DataWriter out = new DataWriter("AltReward_QLearning_Standard.txt");
		String argsString = "-vis off";
		MarioAIOptions marioAIOptions = new MarioAIOptions(argsString);
	    final Agent qAgent = new RLAgent(alpha, gamma, epsilon, LearningType.QLEARNING);
	    marioAIOptions.setAgent(qAgent);
	    //marioAIOptions.setExitProgramWhenFinished(true);
	    BasicTask task = new BasicTask(marioAIOptions);
	    
	    // learning loop
	    ArrayList<Float> fitnesses = new ArrayList<Float>();
	    for (int i = 0; i < iterations; i++) {
	    	task.runSingleEpisode(1);
	    	if (i % 20 == 0) {
	    		((RLAgent)qAgent).setIsLearning(false);
	    		((RLAgent)qAgent).setRewardTotal(0);
	    		float fitness = 0;
	    		float reward = 0;
	    		for (int k = 0; k < 100; k++) {
	    			task.runSingleEpisode(1);
	    			fitness += task.getEvaluationInfo().computeWeightedFitness();
	    			reward += ((RLAgent)qAgent).getRewardTotal();
	    			((RLAgent)qAgent).setRewardTotal(0);
	    		}
	    		//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
	    		fitness /= 100.0f;
	    		reward /= 100.0f;
	    		if (useRewardForLog) {
	    			out.println(""+reward);
	    			System.out.println(""+reward);
	    		} else {
	    			out.println(""+fitness);
	    			System.out.println(""+fitness);
	    		}
	    		//System.out.println(""+fitness);
	    		((RLAgent)qAgent).setIsLearning(true);
	    	}
	    	((RLAgent)qAgent).setRewardTotal(0);
	    	//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
	    }
	    //fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
	    System.out.println("learning complete");
	    // performance test run
	    marioAIOptions.setVisualization(true);
	    marioAIOptions.setFPS(30);
	    task.runSingleEpisode(1);
	    out.closeFile();
	    
	    // run with alternative reward
	    out = new DataWriter("AltReward_QLearning_Alternative.txt");
	    argsString = "-vis off";
	    marioAIOptions = new MarioAIOptions(argsString);
	    final Agent qAgent2 = new RLAgent(alpha, gamma, epsilon, LearningType.QLEARNING);
	    ((RLAgent)qAgent2).useAlternateReward(true);
	    marioAIOptions.setAgent(qAgent2);
	    //marioAIOptions.setExitProgramWhenFinished(true);
	    task = new BasicTask(marioAIOptions);
	    
	    // learning loop
	    for (int i = 0; i < iterations; i++) {
	    	task.runSingleEpisode(1);
	    	if (i % 20 == 0) {
	    		((RLAgent)qAgent2).setIsLearning(false);
	    		((RLAgent)qAgent2).setRewardTotal(0);
	    		float fitness = 0;
	    		float reward = 0;
	    		for (int k = 0; k < 100; k++) {
	    			task.runSingleEpisode(1);
	    			fitness += task.getEvaluationInfo().computeWeightedFitness();
	    			reward += ((RLAgent)qAgent2).getRewardTotal();
	    			((RLAgent)qAgent2).setRewardTotal(0);
	    		}
	    		//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
	    		fitness /= 100.0f;
	    		reward /= 100.0f;
	    		if (useRewardForLog) {
	    			out.println(""+reward);
	    			System.out.println(""+reward);
	    		} else {
	    			out.println(""+fitness);
	    			System.out.println(""+fitness);
	    		}
	    		//System.out.println(""+fitness);
	    		((RLAgent)qAgent2).setIsLearning(true);
	    	}
	    	((RLAgent)qAgent2).setRewardTotal(0);
	    	//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
	    }
	    //fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
	    System.out.println("learning complete");
	    // performance test run
	    marioAIOptions.setVisualization(true);
	    marioAIOptions.setFPS(30);
	    task.runSingleEpisode(1);
	    out.closeFile();
	    
	    
	    
	    
	    //
		//	SARSA
		//
		out = new DataWriter("AltReward_SARSA_Standard.txt");
		argsString = "-vis off";
		marioAIOptions = new MarioAIOptions(argsString);
	    final Agent sarsaAgent = new RLAgent(alpha, gamma, epsilon, LearningType.SARSA);
	    marioAIOptions.setAgent(sarsaAgent);
	    //marioAIOptions.setExitProgramWhenFinished(true);
	    task = new BasicTask(marioAIOptions);
	    
	    // learning loop
	    for (int i = 0; i < iterations; i++) {
	    	task.runSingleEpisode(1);
	    	if (i % 20 == 0) {
	    		((RLAgent)sarsaAgent).setIsLearning(false);
	    		((RLAgent)sarsaAgent).setRewardTotal(0);
	    		float fitness = 0;
	    		float reward = 0;
	    		for (int k = 0; k < 100; k++) {
	    			task.runSingleEpisode(1);
	    			fitness += task.getEvaluationInfo().computeWeightedFitness();
	    			reward += ((RLAgent)sarsaAgent).getRewardTotal();
	    			((RLAgent)sarsaAgent).setRewardTotal(0);
	    		}
	    		//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
	    		fitness /= 100.0f;
	    		reward /= 100.0f;
	    		if (useRewardForLog) {
	    			out.println(""+reward);
	    			System.out.println(""+reward);
	    		} else {
	    			out.println(""+fitness);
	    			System.out.println(""+fitness);
	    		}
	    		//System.out.println(""+fitness);
	    		((RLAgent)sarsaAgent).setIsLearning(true);
	    	}
	    	((RLAgent)sarsaAgent).setRewardTotal(0);
	    	//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
	    }
	    System.out.println("learning complete");
	    // performance test run
	    marioAIOptions.setVisualization(true);
	    marioAIOptions.setFPS(30);
	    task.runSingleEpisode(1);
	    out.closeFile();
	    
	    // run with alternative reward
		out = new DataWriter("AltReward_SARSA_Alternative.txt");
	    argsString = "-vis off";
	    marioAIOptions = new MarioAIOptions(argsString);
	    final Agent sarsaAgent2 = new RLAgent(alpha, gamma, epsilon, LearningType.SARSA);
	    ((RLAgent)sarsaAgent2).useAlternateReward(true);
	    marioAIOptions.setAgent(sarsaAgent2);
	    //marioAIOptions.setExitProgramWhenFinished(true);
	    task = new BasicTask(marioAIOptions);
	    
	    // learning loop
	    for (int i = 0; i < iterations; i++) {
	    	task.runSingleEpisode(1);
	    	if (i % 20 == 0) {
	    		((RLAgent)sarsaAgent2).setIsLearning(false);
	    		((RLAgent)sarsaAgent2).setRewardTotal(0);
	    		float fitness = 0;
	    		float reward = 0;
	    		for (int k = 0; k < 100; k++) {
	    			task.runSingleEpisode(1);
	    			fitness += task.getEvaluationInfo().computeWeightedFitness();
	    			reward += ((RLAgent)sarsaAgent2).getRewardTotal();
	    			((RLAgent)sarsaAgent2).setRewardTotal(0);
	    		}
	    		//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
	    		fitness /= 100.0f;
	    		reward /= 100.0f;
	    		if (useRewardForLog) {
	    			out.println(""+reward);
	    			System.out.println(""+reward);
	    		} else {
	    			out.println(""+fitness);
	    			System.out.println(""+fitness);
	    		}
	    		//System.out.println(""+fitness);
	    		((RLAgent)sarsaAgent2).setIsLearning(true);
	    	}
	    	((RLAgent)sarsaAgent2).setRewardTotal(0);
	    	//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
	    }
	    //fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
	    System.out.println("learning complete");
	    // performance test run
	    marioAIOptions.setVisualization(true);
	    marioAIOptions.setFPS(30);
	    task.runSingleEpisode(1);
	    out.closeFile();
	    
//	    argsString = "-vis off";
//	    marioAIOptions = new MarioAIOptions(argsString);
//	    final Agent sarsaAgent = new RLAgent(0.6f, 0.8f, 0.1f, LearningType.SARSA);
//	    marioAIOptions.setAgent(sarsaAgent);
//	    //marioAIOptions.setExitProgramWhenFinished(true);
//	    task = new BasicTask(marioAIOptions);
//	    
//	    // learning loop
//	    for (int i = 0; i < iterations; i++) {
//	    	task.runSingleEpisode(1);
//	    	System.out.println(i + " " + task.getEvaluationInfo().computeBasicFitness());
//	    }
//	    fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
//	    System.out.println("learning complete");
//	    // performance test run
//	    marioAIOptions.setVisualization(true);
//	    marioAIOptions.setFPS(30);
//	    task.runSingleEpisode(1);
//	    
//	    
//	    System.out.println("\n\n\n\n");
//	    System.out.println("QLearning Fitness: "+fitnesses.get(0));
//	    System.out.println("SARSA Fitness: "+fitnesses.get(1));
	    
	    System.exit(0);
	}
}
