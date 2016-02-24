package com.xb.algoritm.kd;

import java.util.ArrayList;
import java.util.List;

import com.xb.bean.kd.KDNode;

public class KDTreePrinter {

	public static String getString(KDTree tree) {
		if (tree.root == null)
			return "Tree has no nodes.";
		return getString(tree.root, "", true);
	}

	private static String getString(KDNode node, String prefix, boolean isTail) {
		StringBuilder builder = new StringBuilder();

		if (node.parent != null) {
			String side = "left";
			if (node.parent.right != null && node.point.equals(node.parent.right.point))
				side = "right";
			builder.append(prefix + (isTail ? "└── " : "├── ") + "[" + side + "] " + "depth=" + node.depth + " id="
					+ node.point + "\n");
		} else {
			builder.append(prefix + (isTail ? "└── " : "├── ") + "depth=" + node.depth + " id=" + node.point + "\n");
		}
		List<KDNode> children = null;
		if (node.left != null || node.right != null) {
			children = new ArrayList<KDNode>(2);
			if (node.left != null)
				children.add(node.left);
			if (node.right != null)
				children.add(node.right);
		}
		if (children != null) {
			for (int i = 0; i < children.size() - 1; i++) {
				builder.append(getString(children.get(i), prefix + (isTail ? "    " : "│   "), false));
			}
			if (children.size() >= 1) {
				builder.append(getString(children.get(children.size() - 1), prefix + (isTail ? "    " : "│   "), true));
			}
		}

		return builder.toString();
	}
}