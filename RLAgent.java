package com.kmichaelfox.agents.rl;

import java.util.HashMap;
import java.util.Random;

import com.kmichaelfox.agents.rl.LearningAgent;

import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.BasicMarioAIAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.evolution.Evolvable;

// enum Action defined at bottom
// enum LearningType defined at bottom

public class RLAgent extends BasicMarioAIAgent implements LearningAgent {
	public static final int MARIO_BIG 			= Integer.parseInt("000000000001",2);
	public static final int MARIO_FLOWER 		= Integer.parseInt("000000000010",2);
	public static final int MARIO_STUCK			= Integer.parseInt("000000000100",2);
	public static final int ENEMIES_RIGHT_CLOSE = Integer.parseInt("000000001000",2);
	public static final int ENEMIES_RIGHT_FAR	= Integer.parseInt("000000010000",2);
	public static final int ENEMIES_LEFT_CLOSE	= Integer.parseInt("000000100000",2);
	public static final int ENEMIES_LEFT_FAR	= Integer.parseInt("000001000000",2);
	public static final int OBSTACLE_RIGHT		= Integer.parseInt("000010000000",2);
	public static final int OBSTACLE_ABOVE		= Integer.parseInt("000100000000",2);
	public static final int GAP_AHEAD			= Integer.parseInt("001000000000",2);
	public static final int MARIO_ON_GROUND		= Integer.parseInt("010000000000",2);
	public static final int MARIO_CAN_JUMP		= Integer.parseInt("100000000000",2);
	
	private final LearningType type;
	private RLAlgorithm learningProc;
	public static Random rand;
	private float alpha;
	private float gamma;
	private float epsilon;
	
	private int currentState;
	private float currentProgress = 0.0f;
	private float currentFitness = 0.0f;
	private int previousState = 0;
	private float previousProgress = 0.0f;
	private Action previousAction= Action.D;
	private int previousKillsTotal = 0;
	private float previousFitness = 0;
	
	private HashMap<Integer, float[]> qTable;
	
	private int counter = 0;
	private int colCounter = 0;
	private int column = 0;
	
	public RLAgent(float alpha, float gamma, float epsilon, LearningType type) {
		super(type.getName()+"Agent");
		
		rand = new Random();
		qTable = new HashMap<Integer, float[]>();
		
		this.alpha = alpha;
		this.gamma = gamma;
		this.epsilon = epsilon;
		
		this.type = type;
		switch (type) {
		case SARSA:
			learningProc = new SARSA(this);
			break;
			
		case QLEARNING:
			learningProc = new QLearning(this);
			break;
			
		default:
			throw new IllegalArgumentException("LearningType must be either QLEARNING or SARSA");
		}
		
		
		action[Mario.KEY_RIGHT] = true;
	}
	
	public float getAlpha() {
		return alpha;
	}
	
	public float getGamma() {
		return gamma;
	}
	
	public float getEpsilon() {
		return epsilon;
	}
	
	@Override
	public boolean[] getAction() {
		action = new boolean[Environment.numberOfKeys];
		if (((int)marioFloatPos[0]) != column) {
			colCounter = 0;
			column = (int)marioFloatPos[0];
		} else {
			colCounter++;
		}
		
		previousState = currentState;
		previousProgress = currentProgress;
		currentProgress = marioFloatPos[0];
		currentState = 0;
		
		learningProc.execute(); // updates action based on learningProc provided
		
		//System.out.println(qTable.size());
		
//		if (currentState != previousState) {
//			System.out.println(currentState);
//		}
		previousKillsTotal = getKillsTotal;
		previousFitness = currentFitness;
		
		return action;
	}
	
	@Override
	public void integrateObservation(Environment e) {
		super.integrateObservation(e);
		currentFitness = e.getEvaluationInfo().computeWeightedFitness();
	}
	
	public void reset() {
	    action = new boolean[Environment.numberOfKeys];// Empty action
	}

	@Override
	public void giveReward(float reward) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newEpisode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLearningTask(LearningTask learningTask) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEvaluationQuota(long num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Agent getBestAgent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	public float[] getActions(int state) {
		float[] actions = qTable.get(state);
		if (actions == null) {
			actions = new float[12];
			for (int i = 0; i < actions.length; i++) {
				actions[i] = (float)(rand.nextGaussian() * 0.1);
			}
			qTable.put(state, actions);
		}
		
		return actions;
	}
	public int getMarioStatus() {return marioStatus;};
	public int getMarioMode() {return marioMode;};
	public boolean isMarioOnGround() {return isMarioOnGround;};
	public boolean isMarioAbleToJump() {return isMarioAbleToJump;};
	public boolean isMarioAbleToShoot() {return isMarioAbleToShoot;};
	public boolean isMarioCarrying() {return isMarioCarrying;};
	public boolean isMarioStuck() {return colCounter > 35;};
	
	public boolean isEnemyRightClose() {
		return isEnemyAhead(1) || isEnemyAhead(2);
	}
	
	public boolean isEnemyRightFar() {
		return isEnemyAhead(3) || isEnemyAhead(4);
	}
	
	public boolean isEnemyLeftClose() {
		return isEnemyAhead(-1) || isEnemyAhead(-2);
	}
	
	public boolean isEnemyLeftFar() {
		return isEnemyAhead(-3) || isEnemyAhead(-4);
	}
	
	private boolean isEnemyAhead(int stepsAhead) {
		return getEnemiesCellValue(marioEgoRow, marioEgoCol + stepsAhead) != 0 ||
				getEnemiesCellValue(marioEgoRow - 1, marioEgoCol + stepsAhead) != 0;
	}
	
	public boolean isObstacleAbove() {
		return getReceptiveFieldCellValue(marioEgoRow - 2, marioEgoCol) != 0 &&
				getReceptiveFieldCellValue(marioEgoRow - 2, marioEgoCol) != -24 ||
				getReceptiveFieldCellValue(marioEgoRow - 1, marioEgoCol) != 0 &&
				getReceptiveFieldCellValue(marioEgoRow - 1, marioEgoCol) != -24;
	}
	
	public boolean isObstacleAhead() {
		return (getReceptiveFieldCellValue(marioEgoRow, marioEgoCol + 1) != 0 ||
				getReceptiveFieldCellValue(marioEgoRow - 1, marioEgoCol + 1) != 0);
	}
	
	public boolean isGapAhead() {
		return getReceptiveFieldCellValue(marioEgoRow + 1, marioEgoCol + 1) == 0;
	}
	
	public int getKillsTotal() {return getKillsTotal;};
	public int getKillsByFire() {return getKillsByFire;};
	public int getKillsByStomp() {return getKillsByStomp;};
	public int getKillsByShell() {return getKillsByShell;};
	public int getPreviousKillsTotal() {return previousKillsTotal;};

	public int getReceptiveFieldWidth() {return receptiveFieldWidth;};
	public int getReceptiveFieldHeight() {return receptiveFieldHeight;};
	public int getMarioEgoRow() {return marioEgoRow;};
	public int getMarioEgoCol() {return marioEgoCol;};
	
	public int getCurrentState() {return currentState;};
	public float getCurrentProgress() {return currentProgress;};
	public float getCurrentFitness() {return currentFitness;};
	
	public int getPreviousState() {return previousState;};
	public float getPreviousProgress() {return previousProgress;};
	public Action getPreviousAction() {return previousAction;};
	public void setPreviousAction(Action a) {previousAction = a;};
	public float getPreviousFitness() {return previousFitness;};
	
	public void pressKey(int key) {
		action[key] = true;
	}
	
	public void updateState(int bitFlag) {
		currentState += bitFlag;
	}
	
	public void useAlternateReward(boolean b) {
		learningProc.useAlternateReward(b);
	}

	@Override
	public void learn() {
		// TODO Auto-generated method stub
		
	}
}
enum LearningType {
	SARSA("SARSA"), QLEARNING("QLearning");
	
	private String str;
	LearningType(String s) {
		str = s;
	}
	
	public String getName() {
		return str;
	}
}

enum Action {
	L(0), R(1), D(2),
	L_JUMP(3), R_JUMP(4), JUMP(5),
	L_FIRE(6), R_FIRE(7), FIRE(8),
	L_JUMP_FIRE(9), R_JUMP_FIRE(10), JUMP_FIRE(11);
	
	private int value;
	
	Action(int v) {
		value = v;
	}
	
	public int idx() {
		return value;
	}
	
	public static Action getAction(int idx) {
		if (idx > 11 || idx < 0) {
			throw new IllegalArgumentException();
		}
		switch(idx) {
		case 0:
			return L;
		case 1:
			return R;
		case 2:
			return D;
		case 3:
			return L_JUMP;
		case 4:
			return R_JUMP;
		case 5:
			return JUMP;
		case 6:
			return L_FIRE;
		case 7:
			return R_FIRE;
		case 8:
			return FIRE;
		case 9:
			return L_JUMP_FIRE;
		case 10:
			return R_JUMP_FIRE;
		case 11:
		default:
			return JUMP_FIRE;
		}
	}
}

