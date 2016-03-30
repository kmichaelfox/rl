package com.kmichaelfox.agents.rl;

import java.util.ArrayList;

import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.tools.EvaluationInfo;

public class EnvironmentHistory {
	private ArrayList<String> history;
	private DataWriter out;
	
	public EnvironmentHistory() {
		out = new DataWriter("EnvironmentHistory.txt");
		history = new ArrayList<String>();
	}
	
	public void logHistory(Environment e) {
		EvaluationInfo info = e.getEvaluationInfo();
		history.add(info.toStringSingleLine());
	}
	
	public void logHistory(String s) {
		history.add(System.currentTimeMillis() + " " + s);
	}
	
	public void writeHistoryToFile() {
		for (int i = 0; i < history.size(); i++) {
			out.println(history.get(i));
		}
		out.closeFile();
	}
	
	// alternative method - custom struct for POD-based environment history
	private class EnvironmentData {
		
	}
}