package com.xb.bean.kd;

import java.util.Comparator;

public class YPointComparator implements Comparator<XYZPoint>{

	@Override
	public int compare(XYZPoint o1, XYZPoint o2) {
		if (o1.y < o2.y)
			return -1;
		if (o1.y > o2.y)
			return 1;
		return 0;
	}

}
