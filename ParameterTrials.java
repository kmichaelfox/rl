package com.kmichaelfox.agents.rl;

import java.util.ArrayList;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.tasks.BasicTask;
import ch.idsia.tools.MarioAIOptions;

import com.kmichaelfox.agents.es.MLPAgent;

public class ParameterTrials {
	public static void main(String[] args) {
		int iterations = 1000;
		
		float[] alphas = { 0.6f, 0.7f, 0.8f };
		float[] gammas = { 0.6f, 0.7f, 0.8f };
		float[] epsilons = { 0.1f, 0.2f, 0.3f };
		for (int j = 0; j < alphas.length * gammas.length * epsilons.length; j++) {
			DataWriter out = new DataWriter("QLearning_"+alphas[j/3/3]+"_"+gammas[j/3%3]+"_"+epsilons[j%3]+".txt");
			String argsString = "-vis off";
			MarioAIOptions marioAIOptions = new MarioAIOptions(argsString);
		    final Agent qAgent = new RLAgent(alphas[j/3/3], gammas[j/3%3], epsilons[j%3], LearningType.QLEARNING);
		    marioAIOptions.setAgent(qAgent);
		    //marioAIOptions.setExitProgramWhenFinished(true);
		    BasicTask task = new BasicTask(marioAIOptions);
		    
		    // learning loop
		    //int iterations = 5000;
		    //ArrayList<Float> fitnesses = new ArrayList<Float>();
		    for (int i = 0; i < iterations; i++) {
		    	task.runSingleEpisode(1);
		    	if (i % 20 == 0) {
		    		((RLAgent)qAgent).setIsLearning(false);
		    		float fitness = 0;
		    		for (int k = 0; k < 100; k++) {
		    			task.runSingleEpisode(1);
		    			fitness += task.getEvaluationInfo().computeWeightedFitness();
		    		}
		    		//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
		    		fitness /= 100.0f;
		    		out.println(""+fitness);
		    		((RLAgent)qAgent).setIsLearning(true);
		    	}
		    }
		    //fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
		    System.out.println("learning complete");
		    // performance test run
		    //marioAIOptions.setVisualization(true);
		    //marioAIOptions.setFPS(30);
		    //task.runSingleEpisode(1);
		}
	    
	    
		for (int j = 0; j < alphas.length * gammas.length * epsilons.length; j++) {
			DataWriter out = new DataWriter("SARSA_"+alphas[j/3/3]+"_"+gammas[j/3%3]+"_"+epsilons[j%3]+".txt");
			String argsString = "-vis off";
			MarioAIOptions marioAIOptions = new MarioAIOptions(argsString);
		    final Agent qAgent = new RLAgent(alphas[j/3/3], gammas[j/3%3], epsilons[j%3], LearningType.SARSA);
		    marioAIOptions.setAgent(qAgent);
		    //marioAIOptions.setExitProgramWhenFinished(true);
		    BasicTask task = new BasicTask(marioAIOptions);
		    
		    // learning loop
		    //int iterations = 5000;
		    //ArrayList<Float> fitnesses = new ArrayList<Float>();
		    for (int i = 0; i < iterations; i++) {
		    	task.runSingleEpisode(1);
		    	if (i % 20 == 0) {
		    		((RLAgent)qAgent).setIsLearning(false);
		    		float fitness = 0;
		    		for (int k = 0; k < 100; k++) {
		    			task.runSingleEpisode(1);
		    			fitness += task.getEvaluationInfo().computeWeightedFitness();
		    		}
		    		//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
		    		fitness /= 100.0f;
		    		out.println(""+fitness);
		    		((RLAgent)qAgent).setIsLearning(true);
		    	}
		    	//out.println("" + task.getEvaluationInfo().computeWeightedFitness());
		    }
		    //fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
		    System.out.println("learning complete");
		    // performance test run
		    //marioAIOptions.setVisualization(true);
		    //marioAIOptions.setFPS(30);
		    //task.runSingleEpisode(1);
		}
//	    String argsString = "-vis off";
//	    MarioAIOptions marioAIOptions = new MarioAIOptions(argsString);
//	    final Agent sarsaAgent = new RLAgent(0.8f, 0.6f, 0.1f, LearningType.SARSA);
//	    marioAIOptions.setAgent(sarsaAgent);
//	    //marioAIOptions.setExitProgramWhenFinished(true);
//	    BasicTask task = new BasicTask(marioAIOptions);
//	    
//	    // learning loop
//	    for (int i = 0; i < iterations; i++) {
//	    	task.runSingleEpisode(1);
//	    	System.out.println(i + " " + task.getEvaluationInfo().computeBasicFitness());
//	    }
//	    //fitnesses.add((float)task.getEvaluationInfo().computeBasicFitness());
//	    System.out.println("learning complete");
//	    // performance test run
//	    marioAIOptions.setVisualization(true);
//	    marioAIOptions.setFPS(30);
//	    task.runSingleEpisode(1);
		
		System.exit(0);
	}
}
