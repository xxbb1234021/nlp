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
	 * Computes the Euclidean distance from this point to the other.
	 * 
	 * @param o1
	 *            other point.
	 * @return euclidean distance.
	 */
	public double euclideanDistance(XYZPoint o1) {
		return euclideanDistance(o1, this);
	}

	/**
	 * Computes the Euclidean distance from one point to the other.
	 * 
	 * @param o1
	 *            first point.
	 * @param o2
	 *            second point.
	 * @return euclidean distance.
	 */
	private static final double euclideanDistance(XYZPoint o1, XYZPoint o2) {
		return Math.sqrt(Math.pow((o1.x - o2.x), 2)
				+ Math.pow((o1.y - o2.y), 2) + Math.pow((o1.z - o2.z), 2));
	};

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(XYZPoint o) {
		int xComp = new XPointComparator().compare(this, o);
		if (xComp != 0)
			return xComp;
		int yComp = new YPointComparator().compare(this, o);
		return yComp;
	}

	/**
	 * {@inheritDoc}
	 */
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