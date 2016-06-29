package com.xb.utils.pinyin;

class BranchNode extends TrieNode {
	BranchNode(char k) {
		this.key = k;
		this.kind = NodeKind.BN;
		this.points = new TrieNode[27];
	}
}