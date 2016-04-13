package com.xb.bean.syntax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/4/6.
 */
public class SytaxSubInfo {
	private List<SyntaxIndexesLink> syntaxIndexesLinkList = new ArrayList<SyntaxIndexesLink>();
	private List<XYPoint> pointList = new ArrayList<XYPoint>();

    public List<SyntaxIndexesLink> getSyntaxIndexesLinkList() {
        return syntaxIndexesLinkList;
    }

    public void setSyntaxIndexesLinkList(List<SyntaxIndexesLink> syntaxIndexesLinkList) {
        this.syntaxIndexesLinkList = syntaxIndexesLinkList;
    }

    public List<XYPoint> getPointList() {
		return pointList;
	}

	public void setPointList(List<XYPoint> pointList) {
		this.pointList = pointList;
	}
}
