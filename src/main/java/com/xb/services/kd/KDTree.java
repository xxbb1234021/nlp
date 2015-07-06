package com.xb.services.kd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.xb.bean.kd.EuclideanComparator;
import com.xb.bean.kd.KDNode;
import com.xb.bean.kd.XPointComparator;
import com.xb.bean.kd.XYZPoint;
import com.xb.bean.kd.YPointComparator;
import com.xb.bean.kd.ZPointComparator;

public class KDTree
{
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int Z_AXIS = 2;

	public int k = 3;
	public KDNode root = null;

	public KDTree()
	{
	}

	public KDTree(List<XYZPoint> list)
	{
		root = createKDNode(list, k, 0);
	}

	/**
	 * 创建树
	 * @param list
	 * @param k
	 * @param depth
	 * @return
	 */
	public KDNode createKDNode(List<XYZPoint> list, int k, int depth)
	{
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
		if (list.size() > 0)
		{
			if ((mediaIndex - 1) >= 0)
			{
				List<XYZPoint> less = list.subList(0, mediaIndex);
				if (less.size() > 0)
				{
					node.left = createKDNode(less, k, depth + 1);
					node.left.parent = node;
				}
			}
			if ((mediaIndex + 1) <= (list.size() - 1))
			{
				List<XYZPoint> more = list.subList(mediaIndex + 1, list.size());
				if (more.size() > 0)
				{
					node.right = createKDNode(more, k, depth + 1);
					node.right.parent = node;
				}
			}
		}

		return node;
	}

	/**
	 * 查找树
	 * @param k
	 * @param value
	 * @return
	 */
	public Collection<XYZPoint> search(int k, XYZPoint value)
	{
		if (value == null)
			return null;

		TreeSet<KDNode> results = new TreeSet<KDNode>(new EuclideanComparator(value));
		KDNode prev = null;
		KDNode node = root;
		while (node != null)
		{
			if (KDNode.compareTo(node.depth, node.k, node.point, value) < 0)
			{
				prev = node;
				node = node.right;
			}
			else
			{
				prev = node;
				node = node.left;
			}
		}
		KDNode leaf = prev;

		if (leaf != null)
		{
			Set<KDNode> examined = new HashSet<KDNode>();

			node = leaf;
			while (node != null)
			{
				//Search node  
				searchNode(k, value, node, results, examined);
				node = node.parent;
			}
		}

		Collection<XYZPoint> collection = new ArrayList<XYZPoint>(k);
		for (KDNode kdNode : results)
		{
			collection.add(kdNode.point);
		}
		return collection;
	}

	/**
	 * 查找节点
	 * @param <T>
	 * @param k
	 * @param value
	 * @param node
	 * @param results
	 * @param examined
	 */
	private void searchNode(int k, XYZPoint value, KDNode node, TreeSet<KDNode> results, Set<KDNode> examined)
	{
		examined.add(node);

		KDNode lastNode = null;
		Double lastDistance = Double.MAX_VALUE;
		if (results.size() > 0)
		{
			lastNode = results.last();
			lastDistance = lastNode.point.euclideanDistance(value);
		}

		Double nodeDistance = node.point.euclideanDistance(value);
		if (nodeDistance.compareTo(lastDistance) < 0)
		{
			if (results.size() == k && lastNode != null)
				results.remove(lastNode);
			results.add(node);
		}
		else if (nodeDistance.equals(lastDistance))
		{
			results.add(node);
		}
		else if (results.size() < k)
		{
			results.add(node);
		}

		lastNode = results.last();
		lastDistance = lastNode.point.euclideanDistance(value);

		int axis = node.depth % node.k;
		KDNode left = node.left;
		KDNode right = node.right;

		if (left != null && !examined.contains(left))
		{
			examined.add(left);

			double nodePoint = Double.MIN_VALUE;
			double valuePlusDistance = Double.MIN_VALUE;
			if (axis == X_AXIS)
			{
				nodePoint = node.point.x;
				valuePlusDistance = value.x - lastDistance;
			}
			else if (axis == Y_AXIS)
			{
				nodePoint = node.point.y;
				valuePlusDistance = value.y - lastDistance;
			}
			else
			{
				nodePoint = node.point.z;
				valuePlusDistance = value.z - lastDistance;
			}
			boolean lineIntersectsCube = ((valuePlusDistance <= nodePoint) ? true : false);
 
			if (lineIntersectsCube)
				searchNode(k, value, left, results, examined);
		}
		if (right != null && !examined.contains(right))
		{
			examined.add(right);

			double nodePoint = Double.MIN_VALUE;
			double valuePlusDistance = Double.MIN_VALUE;
			if (axis == X_AXIS)
			{
				nodePoint = node.point.x;
				valuePlusDistance = value.x + lastDistance;
			}
			else if (axis == Y_AXIS)
			{
				nodePoint = node.point.y;
				valuePlusDistance = value.y + lastDistance;
			}
			else
			{
				nodePoint = node.point.z;
				valuePlusDistance = value.z + lastDistance;
			}
			boolean lineIntersectsCube = ((valuePlusDistance >= nodePoint) ? true : false);
 
			if (lineIntersectsCube)
				searchNode(k, value, right, results, examined);
		}
	}

	@Override
	public String toString()
	{
		return KDTreePrinter.getString(this);
	}

	public static void main(String[] args)
	{
		java.util.List<XYZPoint> points = new ArrayList<XYZPoint>();
		XYZPoint p1 = new XYZPoint(2, 3);
		points.add(p1);
		XYZPoint p2 = new XYZPoint(5, 4);
		points.add(p2);
		XYZPoint p3 = new XYZPoint(9, 6);
		points.add(p3);
		XYZPoint p4 = new XYZPoint(4, 7);
		points.add(p4);
		XYZPoint p5 = new XYZPoint(8, 1);
		points.add(p5);
		XYZPoint p6 = new XYZPoint(7, 2);
		points.add(p6);
		KDTree kdTree = new KDTree(points);

		System.out.println(kdTree.toString());

		XYZPoint search = new XYZPoint(1, 4);
		Collection<XYZPoint> result = kdTree.search(4, search);
		System.out.println("NNS for "+search+" result="+result+"\n"); 
	}
}
