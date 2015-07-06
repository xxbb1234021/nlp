package com.xb.bean.kd;


public class XYZPoint implements Comparable<XYZPoint> {

	public double x = Double.NEGATIVE_INFINITY;
	public double y = Double.NEGATIVE_INFINITY;
	public double z = Double.NEGATIVE_INFINITY;

	public XYZPoint(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	public XYZPoint(double x, int y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * 比较距离
	 * @param o1
	 * @return
	 */
	public double euclideanDistance(XYZPoint o1) {
		return euclideanDistance(o1, this);
	}

	/**
	 * 计算距离公式
	 * @param o1
	 * @param o2
	 * @return
	 */
	private static final double euclideanDistance(XYZPoint o1, XYZPoint o2) {
		return Math.sqrt(Math.pow((o1.x - o2.x), 2)
				+ Math.pow((o1.y - o2.y), 2) + Math.pow((o1.z - o2.z), 2));
	};

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof XYZPoint))
			return false;

		XYZPoint xyzPoint = (XYZPoint) obj;
		int xComp = new XPointComparator().compare(this, xyzPoint);
		if (xComp != 0)
			return false;
		int yComp = new YPointComparator().compare(this, xyzPoint);
		return (yComp == 0);
	}

	@Override
	public int compareTo(XYZPoint o) {
		int xComp = new XPointComparator().compare(this, o);
		if (xComp != 0)
			return xComp;
		int yComp = new YPointComparator().compare(this, o);
		return yComp;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(x).append(", ");
		builder.append(y).append(", ");
		builder.append(z);
		builder.append(")");
		return builder.toString();
	}
}