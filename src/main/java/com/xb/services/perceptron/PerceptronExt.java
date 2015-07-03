package com.xb.services.perceptron;

import java.util.Scanner;

public class PerceptronExt
{
	private static int N = 3;
	private static int n = 2;
	private static double[][] X = null;
	private static double[] Y = null;
	private static double[][] G = null;
	private static double[] A = null;
	private static double[] W = null;
	private static double B = 0;
	private static double fi = 0.5;

	private static boolean check(int id)
	{
		double ans = B;
		for (int i = 0; i < N; i++)
			ans += A[i] * Y[i] * G[i][id];
		if (ans * Y[id] > 0)
			return true;
		return false;
	}

	public static void solve()
	{
		Scanner in = new Scanner(System.in);
		System.out.print("input N:");
		N = in.nextInt();
		System.out.print("input n:");
		n = in.nextInt();

		X = new double[N][n];
		Y = new double[N];
		G = new double[N][N];

		System.out.println("input N * n datas X[i][j]:");
		for (int i = 0; i < N; i++)
			for (int j = 0; j < n; j++)
				X[i][j] = in.nextDouble();
		System.out.println("input N datas Y[i]");
		for (int i = 0; i < N; i++)
			Y[i] = in.nextDouble();

		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
			{
				G[i][j] = 0;
				for (int k = 0; k < n; k++)
					G[i][j] += X[i][k] * X[j][k];
			}

		A = new double[N];
		W = new double[n];
		for (int i = 0; i < n; i++)
			A[i] = 0;
		B = 0;

		boolean ok = true;
		while (ok == true)
		{
			ok = false;
			//这里在原来算法的基础上不断地将fi缩小，以避免跳来跳去一直达不到要求的点的效果。
			for (int i = 0; i < N; i++)
			{
				//System.out.println("here " + i);
				while (check(i) == false)
				{
					ok = true;
					A[i] += fi;
					B += fi * Y[i];
					//debug();
				}
			}
			fi *= 0.5;
		}

		for (int i = 0; i < n; i++)
			W[i] = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < n; j++)
				W[j] += A[i] * Y[i] * X[i][j];
	}

	public static void main(String[] args)
	{
		solve();
		System.out.print("W = [");
		for (int i = 0; i < n - 1; i++)
			System.out.print(W[i] + ", ");
		System.out.println(W[n - 1] + "]");
		System.out.println("B = " + B);
	}
}
