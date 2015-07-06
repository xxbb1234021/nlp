package com.xb.bean.kd;

import java.util.Comparator;

public class EuclideanComparator implements Comparator<KDNode> {

	private XYZPoint point = null;

	public EuclideanComparator(XYZPoint point) {
		this.point = point;
	}

	@Override
	public int compare(KDNode o1, KDNode o2) {
		Double d1 = point.euclideanDistance(o1.point);
		Double d2 = point.euclideanDistance(o2.point);
		if (d1.compareTo(d2) < 0)
			return -1;
		else if (d2.compareTo(d1) < 0)
			return 1;
		return o1.point.compareTo(o2.point);
	}
};  