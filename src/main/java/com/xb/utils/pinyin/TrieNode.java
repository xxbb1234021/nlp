package com.xb.utils.pinyin;

class TrieNode {
	char key;
	TrieNode[] points = null;
	NodeKind kind = null;
}

enum NodeKind {
	LN, BN;
}