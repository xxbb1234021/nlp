package com.xb.bean.kd;

import java.util.Comparator;

public class ZPointComparator implements Comparator<XYZPoint>{

	@Override
	public int compare(XYZPoint o1, XYZPoint o2) {
		if (o1.z < o2.z)
			return -1;
		if (o1.z > o2.z)
			return 1;
		return 0;
	}

}
