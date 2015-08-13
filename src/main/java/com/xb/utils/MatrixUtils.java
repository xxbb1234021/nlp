package com.xb.utils;

public class MatrixUtils {
	
	public static double[][] dMatrix(int nrl, int nrh, int ncl, int nch) {
		//double[][] matrix = new double[nrh - nrl + 1][nch - ncl + 1];
		double[][] matrix = new double[nrh + 1][nch + 1];
		return matrix;
	}
}
