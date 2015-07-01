package com.xb.utils;
//感知机学习算法  
//感知机模型 f(x) = sign(w*x+b)  
public class Perceptron
{
	int m_learnRate;//学习率  
	int m_w0;
	int m_w1;
	int m_b;

	public Perceptron(int w0, int w1, int b0, int learnRate)
	{
		this.m_b = b0;
		this.m_learnRate = learnRate;
		this.m_w0 = w0;
		this.m_w1 = w1;
	}

	/* 
	 * 判断针对训练数据x 估测的模型与实际数据是否有误差 
	 */
	private boolean judgeHasError(int[] x)
	{
		//如果表达式小于0，说明没有被正确分类  
		if ((x[2] * (this.m_w0 * x[0] + this.m_w1 * x[1] + this.m_b)) <= 0)
			return false;
		return true;
	}

	/* 
	 * 有误差的话，需要调整模型参数 
	 */
	private void adjustParam(int[] x)
	{
		//根据梯度下降法调整参数 w b  
		this.m_w0 = this.m_w0 + this.m_learnRate * x[2] * x[0];
		this.m_w1 = this.m_w1 + this.m_learnRate * x[2] * x[1];
		this.m_b = this.m_b + this.m_learnRate * x[2];
		return;
	}

	public void TrainData(int data[][], int num) throws InterruptedException
	{
		int count = 0;
		boolean isOver = false;
		while (!isOver)
		{
			System.out.println("w0 w1 b: " + this.m_w0 + " " + this.m_w1 + " " + this.m_b);

			for (int i = 0; i < num; ++i)
			{
				if (!judgeHasError(data[i]))
				{
					System.out.println(i + "调整次数：" + (++count));
					adjustParam(data[i]);
					isOver = false;
					break;
				}
				else
					isOver = true;
			}
		}
		//  
		System.out.println("w0 w1 b: " + this.m_w0 + " " + this.m_w1 + " " + this.m_b);
	}

	public static void main(String args[]) throws Exception
	{
		//data数组中包括 正实例点和负实例点，其中数组中最后一位元素代表其为何种实例点（1代表正实例，-1代表负实例）  
		//训练数据一共包括三组，前两组是正实例  
		int data[][] = { { 3, 3, 1 }, { 4, 3, 1 }, { 1, 1, -1 } };
		Perceptron p = new Perceptron(0, 0, 0, 1);
		p.TrainData(data, 3);
	}
}