package com.xb.algoritm.perceptron;

import java.util.ArrayList;
import java.util.List;

import com.xb.utils.FileUtil;

public class PerceptronExt {
	int n = 1;
	int b = 0;
	int[] a = null;
	int[] w = null;
	int[][] x = null;
	int[][] gram = null;

	public PerceptronExt() {
		x = readFile();
		a = new int[x.length];
		for (int i = 0; i < x.length; i++) {
			a[i] = 0;
		}

		w = new int[x.length - 1];
		for (int i = 0; i < x.length - 1; i++) {
			w[i] = 0;
		}

		gram = createGram(x);
	}

	public void select(int[][] x, int[] a, int b, int[][] gram) {
		int i = 0;
		while (i < x.length) {
			if (x[i][2]
					* ((a[0] * x[0][2] * gram[0][i] + a[1] * x[1][2] * gram[1][i] + a[2] * x[2][2] * gram[2][i]) + b) <= 0) {
				a[i] += 1;
				b += x[i][2];
				i = 0;
			} else {
				i++;
			}
		}
		System.out.println("a的值:" + a[0] + " " + a[1] + " " + a[2] + ",b的值" + b);
	}

	public int[][] readFile() {
		String fileName = getClass().getResource("/").getPath() + "trainFile/PerceptronTrainFile.utf8";
		return FileUtil.readPerceptronFile(fileName);
	}

	public int[][] createGram(int[][] x) {
		List<int[]> list = new ArrayList<int[]>();
		int[] temp = new int[x.length];
		int s = 0;
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x.length; j++) {
				for (int k = 0; k < x[i].length - 1; k++) {
					//System.out.println(x[i][k]);
					//System.out.println(x[j][k]);
					s += x[i][k] * x[j][k];
				}
				//System.out.print(s + "  ");
				temp[j] = s;
				s = 0;

				//System.out.println();
			}
			list.add(temp);
			temp = new int[x.length];
		}

		return (int[][]) list.toArray(new int[0][0]);
	}

	public int cal(int index) {
		int res = 0;
		for (int i = 0; i < x.length; i++) {
			res += a[i] * x[i][2] * gram[i][index];
		}
		res += b;
		res *= x[index][2];
		return res;
	}

	public void update(int index) {
		a[index] += n;
		b = b + n * x[index][2];
		System.out.print("[");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.print("]");
		System.out.print("    " + b);
		System.out.println();
	}

	public int[] numProduct(int num, int[] vec) {
		for (int j = 0; j < vec.length; j++) {
			vec[j] *= num;
		}
		return vec;
	}

	public int[] addVector(int[] vec1, int[] vec2) {
		for (int j = 0; j < vec2.length; j++) {
			vec1[j] = vec1[j] + vec2[j];
		}

		return vec1;
	}

	public void check() {
		boolean flag = false;
		for (int i = 0; i < x.length; i++) {
			if (cal(i) <= 0) {
				flag = true;
				update(i);
			}
		}

		if (!flag) {
			for (int i = 0; i < x.length; i++) {
				w = addVector(w, numProduct(a[i] * x[i][2], new int[] { x[i][0], x[i][1] }));
			}
			System.out.println("RESULT: w: " + w[0] + " " + w[1] + " b: " + b);
			System.exit(0);
		}

	}

	public static void main(String[] args) {
		PerceptronExt t = new PerceptronExt();
		//				int[][] x = { { 3, 3, 1 }, { 4, 3, 1 }, { 1, 1, -1 } };
		//				int[] a = { 0, 0, 0 };
		//				int b = 0;
		//				int[][] gram = { { 18, 21, 6 }, { 21, 25, 7 }, { 6, 7, 2 } };
		//				t.select(x, a, b, gram);

		for (int a = 0; a < 1000; a++) {
			t.check();
		}
		//t.select(x, a, b, gram);
	}
}
