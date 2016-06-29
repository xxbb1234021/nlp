package com.xb.bean.tree;

import java.util.List;

/**
 * Created by kevin on 2016/6/6.
 */
public class TreeRoot {

	private String name;

	private List<TreeRoot> children;

	public TreeRoot() {

	}

	public TreeRoot(String name) {
		this.name = name;
	}

	public TreeRoot(String name, List<TreeRoot> children) {
		this.name = name;
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeRoot> getChildren() {
		return children;
	}

	public void setChildren(List<TreeRoot> children) {
		this.children = children;
	}
}
