package com.xb.bean.trie;

import java.util.LinkedList;

/**
 * Created by kevin on 2016/3/25.
 */
public class SyntaxTrieNode extends TrieNode{
	private String key;
	private boolean bound;
	private int count;
	private double prob;
	private int level;

	private LinkedList<SyntaxTrieNode> childList = new LinkedList<SyntaxTrieNode>();
	private SyntaxTrieNode parent;

	public SyntaxTrieNode() {
	}

	public SyntaxTrieNode(String c) {
		this.key = c;
	}

	public SyntaxTrieNode subNode(String c) {
		if (childList != null) {
			for (SyntaxTrieNode eachChild : childList) {
				if (eachChild.key.equals(c)) {
					return eachChild;
				}
			}
		}
		return null;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isBound() {
		return bound;
	}

	public void setBound(boolean bound) {
		this.bound = bound;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getProb() {
		return prob;
	}

	public void setProb(double prob) {
		this.prob = prob;
	}

	public LinkedList<SyntaxTrieNode> getChildList() {
		return childList;
	}

	public void setChildList(LinkedList<SyntaxTrieNode> childList) {
		this.childList = childList;
	}

	public SyntaxTrieNode getParent() {
		return parent;
	}

	public void setParent(SyntaxTrieNode parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
