package com.xb.bean.kd;

import java.util.Comparator;

public class XPointComparator implements Comparator<XYZPoint>{

	@Override
	public int compare(XYZPoint o1, XYZPoint o2) {
		if (o1.x < o2.x)
			return -1;
		if (o1.x > o2.x)
			return 1;
		return 0;
	}

}
