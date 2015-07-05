package com.xb.services.kd;

import java.util.Collections;
import java.util.List;

import com.xb.bean.kd.KDNode;
import com.xb.bean.kd.XPointComparator;
import com.xb.bean.kd.XYZPoint;
import com.xb.bean.kd.YPointComparator;
import com.xb.bean.kd.ZPointComparator;

public class KDTree<T extends XYZPoint> {
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int Z_AXIS = 2;

	private int k = 3;
	private KDNode root = null;

	public KDTree() {
	}

	public KDTree(List<XYZPoint> list) {
		root = createKDNode(list, k, 0);
	}

	public KDNode createKDNode(List<XYZPoint> list, int k, int depth) {
		if (list == null || list.size() == 0)
			return null;

		int axis = depth % k;
		if (axis == X_AXIS)
			Collections.sort(list, new XPointComparator());
		else if (axis == Y_AXIS)
			Collections.sort(list, new YPointComparator());
		else
			Collections.sort(list, new ZPointComparator());

		int mediaIndex = list.size() / 2;
		KDNode node = new KDNode(k, depth, list.get(mediaIndex));
		if (list.size() > 0) {
			if ((mediaIndex - 1) >= 0) {
				List<XYZPoint> less = list.subList(0, mediaIndex);
				if (less.size() > 0) {
					node.left = createKDNode(less, k, depth + 1);
					node.left.parent = node;
				}
			}
			if ((mediaIndex + 1) <= (list.size() - 1)) {
				List<XYZPoint> more = list.subList(mediaIndex + 1, list.size());
				if (more.size() > 0) {
					node.right = createKDNode(more, k, depth + 1);
					node.right.parent = node;
				}
			}
		}

		return node;
	}
}
