package com.xb.bean.tree;

import java.util.List;

/**
 * Created by kevin on 2016/6/6.
 */
public class TreeChildren {
	private String name;

	private List<TreeChildren> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TreeChildren> getChildren() {
		return children;
	}

	public void setChildren(List<TreeChildren> children) {
		this.children = children;
	}
}
