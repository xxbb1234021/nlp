package com.xb.bean.trie;

import java.util.HashMap;

public class TrieNode
{
	/** 结点关键字，其值为中文词中的一个字 */
	private char key = '\0';

	/** 如果该字在词语的末尾，则bound=true */
	private boolean bound = false;

	/** 指向下一个结点的指针结构，用来存放当前字在词中的下一个字的位置 */
	private HashMap<Character, TrieNode> childs = new HashMap<Character, TrieNode>();
	
	/**  单词出现频数 */
	public int count;

	public TrieNode()
	{
	}

	public TrieNode(char key)
	{
		this.key = key;
	}

	public char getKey()
	{
		return key;
	}

	public void setKey(char key)
	{
		this.key = key;
	}

	public boolean isBound()
	{
		return bound;
	}

	public void setBound(boolean bound)
	{
		this.bound = bound;
	}

	public HashMap<Character, TrieNode> getChilds()
	{
		return childs;
	}

	public void setChilds(HashMap<Character, TrieNode> childs)
	{
		this.childs = childs;
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}
}