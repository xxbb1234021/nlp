package com.xb.bean.kd;

import com.xb.services.kd.KDTree;

public class KDNode implements Comparable<KDNode>{
	public int k = 3;  
	public int depth = 0;  
    public XYZPoint point = null;  
	public KDNode parent = null;  
	public KDNode left = null;  
	public KDNode right = null;  


    public KDNode(XYZPoint point) {  
        this.point = point;  
    }  

    public KDNode(int k, int depth, XYZPoint point) {  
        this(point);  
        this.k = k;  
        this.depth = depth;  
    }  

	public static int compareTo(int depth, int k, XYZPoint o1, XYZPoint o2) {
		int axis = depth % k;
		if (axis == KDTree.X_AXIS)
			return new XPointComparator().compare(o1, o2);
		
		return new YPointComparator().compare(o1, o2);
	}  
 
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof KDNode))
			return false;

		KDNode KDNode = (KDNode) obj;
		if (this.compareTo(KDNode) == 0)
			return true;

		return false;
	}  
 
    @Override  
	public int compareTo(KDNode node) {  
        return compareTo(depth, k, this.point, node.point);  
    }  
 
    @Override  
    public String toString() {  
        StringBuilder builder = new StringBuilder();  
        builder.append("k=").append(k);  
        builder.append(" depth=").append(depth);  
        builder.append(" id=").append(point.toString());  
        return builder.toString();  
    }
}
