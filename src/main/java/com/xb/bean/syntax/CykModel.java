package com.xb.bean.syntax;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/4/9.
 */
public class CykModel {
	private List<SyntaxProbLink>[][] probLinkArray;
	private List<SyntaxIndexesLink>[][] indexLinkArray;

    public CykModel(int len) {
        //初始化矩阵
        probLinkArray = new ArrayList[len][len];
        indexLinkArray = new ArrayList[len][len];

        for (int i = 0; i < probLinkArray.length; i++) {
            for (int j = 0; j < probLinkArray[i].length; j++) {
                probLinkArray[i][j] = new ArrayList<SyntaxProbLink>();
                indexLinkArray[i][j] = new ArrayList<SyntaxIndexesLink>();
            }
        }
    }

    public List<SyntaxProbLink>[][] getProbLinkArray() {
		return probLinkArray;
	}

	public void setProbLinkArray(List<SyntaxProbLink>[][] probLinkArray) {
		this.probLinkArray = probLinkArray;
	}

	public List<SyntaxIndexesLink>[][] getIndexLinkArray() {
		return indexLinkArray;
	}

	public void setIndexLinkArray(List<SyntaxIndexesLink>[][] indexLinkArray) {
		this.indexLinkArray = indexLinkArray;
	}
}
