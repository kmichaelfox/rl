package com.kmichaelfox.agents.rl;

import java.util.ArrayList;
import java.util.Random;

import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class SARSA implements RLAlgorithm {
	private RLAgent agent;
	private Random rand;
	
	public SARSA(RLAgent agent) {
		this.agent = agent;
		rand = new Random();
	}
	
	public void execute() {
		// get new state
		findCurrentState();
		
		// apply reward to previous state
		float[] previousActions = agent.getActions(agent.getPreviousState());
		float[] nextActions = agent.getActions(agent.getCurrentState());
		
		int nextMaxQIdx = findNextMaxQAction(nextActions);
		if (rand.nextFloat() < agent.getEpsilon()) {
			nextMaxQIdx = rand.nextInt(12);
		}
		
		float reward = calculateReward(agent.getPreviousState(), agent.getCurrentState());
		previousActions[agent.getPreviousAction().idx()] = previousActions[agent.getPreviousAction().idx()] + agent.getAlpha() * (reward + agent.getGamma() * nextActions[nextMaxQIdx] - previousActions[agent.getPreviousAction().idx()]);
		
		// take action for next state, first check for chance of exploration
		applyAction(Action.getAction(nextMaxQIdx));
		agent.setPreviousAction(Action.getAction(nextMaxQIdx));
	}
	
	private void findCurrentState() {
		if (agent.getMarioMode() > 0) {
			agent.updateState(RLAgent.MARIO_BIG);
		}
		
		if (agent.getMarioMode() == 2) {
			agent.updateState(RLAgent.MARIO_FLOWER);
		}
		
		if (agent.isMarioStuck()) {
			agent.updateState(RLAgent.MARIO_STUCK);
		}
		
		if (agent.isEnemyRightClose()) {
			agent.updateState(RLAgent.ENEMIES_RIGHT_CLOSE);
		}
		
		if (agent.isEnemyRightFar()) {
			agent.updateState(RLAgent.ENEMIES_RIGHT_FAR);
		}
		
		if (agent.isEnemyLeftClose()) {
			agent.updateState(RLAgent.ENEMIES_LEFT_CLOSE);
		}
		
		if (agent.isEnemyLeftFar()) {
			agent.updateState(RLAgent.ENEMIES_LEFT_FAR);
		}
		
		if (agent.isObstacleAhead()) {
			agent.updateState(RLAgent.OBSTACLE_RIGHT);
		}
		
		if (agent.isObstacleAbove()) {
			agent.updateState(RLAgent.OBSTACLE_ABOVE);
		}
		
		if (agent.isGapAhead()) {
			agent.updateState(RLAgent.GAP_AHEAD);
		}
		
		if (agent.isMarioOnGround()) {
			agent.updateState(RLAgent.MARIO_ON_GROUND);
		}
		
		if (agent.isMarioAbleToJump()) {
			agent.updateState(RLAgent.MARIO_CAN_JUMP);
		}
	}
	
	private int findNextMaxQAction(float[] actions) {
		// ArrayList of indices with equal Q values
		ArrayList<Integer> indices = new ArrayList<Integer>();
		indices.add(0);
		for (int i = 1; i < actions.length; i++) {
			if (actions[i] > actions[indices.get(0)]) {
				indices.clear();
				indices.add(i);
			} else if (actions[i] == actions[indices.get(0)]) {
				indices.add(i);
			}
		}
		return indices.get(rand.nextInt(indices.size()));
	}
	
	private float calculateReward(int previousState, int currentState) {
		if (agent.getMarioStatus() == Mario.STATUS_DEAD) {
			return -100.0f;
		} else if (agent.getMarioStatus() == Mario.STATUS_WIN) {
			return 100.0f;
		}
		
		if ((currentState&RLAgent.MARIO_STUCK)==RLAgent.MARIO_STUCK) {
			return -10.0f;
		}
		
		if (agent.getPreviousProgress() > agent.getCurrentProgress()) {
			//System.out.println("moving backward");
			return -1.0f;
		} else if (agent.getPreviousProgress() < agent.getCurrentProgress()) {
			return 5.0f;
		}
		
		if (agent.getPreviousKillsTotal() < agent.getKillsTotal()) {
			return 50.0f;
		}
		
		return 0.0f;
	};
	
	private void applyAction(Action a) {
		// set direction
		switch (a) {
		case L:
		case L_JUMP:
		case L_FIRE:
		case L_JUMP_FIRE:
			agent.pressKey(Mario.KEY_LEFT);
			break;
		case R:
		case R_JUMP:
		case R_FIRE:
		case R_JUMP_FIRE:
			agent.pressKey(Mario.KEY_RIGHT);
			break;
		case D:
			agent.pressKey(Mario.KEY_DOWN);
			break;
		default:
			break;
		}
		
		// set jump modifier
		if (a == Action.JUMP || a == Action.L_JUMP || a == Action.R_JUMP || 
				a == Action.JUMP_FIRE || a == Action.L_JUMP_FIRE || a == Action.R_JUMP_FIRE) {
			agent.pressKey(Mario.KEY_JUMP);
		}
		
		// set fire modifier
		if (a == Action.FIRE || a == Action.L_FIRE || a == Action.R_FIRE || 
				a == Action.JUMP_FIRE || a == Action.L_JUMP_FIRE || a == Action.R_JUMP_FIRE) {
			agent.pressKey(Mario.KEY_SPEED);
		}
	}
}
