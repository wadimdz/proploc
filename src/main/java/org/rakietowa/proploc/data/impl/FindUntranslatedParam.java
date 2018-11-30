package org.rakietowa.proploc.data.impl;

public class FindUntranslatedParam {
	private String previousDir;
	private String currentDir;
	
	public FindUntranslatedParam(String prev, String curr) {
		previousDir = prev;
		currentDir = curr;
	}

	public String getPreviousDir() {
		return previousDir;
	}

	public void setPreviousDir(String previousDir) {
		this.previousDir = previousDir;
	}

	public String getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(String currentDir) {
		this.currentDir = currentDir;
	}
}
