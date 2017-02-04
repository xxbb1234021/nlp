package com.xb.algoritm.neuralnet;

import com.xb.bean.neuron.Neuron;
import com.xb.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/1/13 0013.
 */
public class BPNeuralNet {
    //学习率
    private static final double RATE = 0.2;
    //误差停止阈值
    private static final double STOP = 0.005;
    //迭代次数阈值
    private static final int NUMBER_ROUND = 60000;

    //神经网络
    private List<List<Neuron>> neuronList;
    //网络权重, weight[i][j][k] = 第i层和第i+1层之间, j神经元和k神经元的权重, i = 0 ... layer-1
    private double[][][] weight;
    //下一层网络残差, deta[i][j] = 第i层, 第j神经元的残差, i = 1 ... layer-1
    private double[][] deta;
    //网络层数（包括输入与输出层）
    private int layer;
    //包括每层神经元个数
    private int[] array;

    public BPNeuralNet(int[] array) {
        init(array);
    }

    public void init(int[] array) {
        this.array = array;
        this.layer = array.length;
        this.neuronList = new ArrayList<List<Neuron>>();

        initNeurons();
        initWeight();
        initDeta();
    }

    /**
     * 初始化神经元
     */
    private void initNeurons() {
        for (int i = 0; i < this.layer; i++) {
            List<Neuron> list = new ArrayList<Neuron>();
            for (int j = 0; j < this.array[i]; j++) {
                list.add(new Neuron());
            }
            this.neuronList.add(list);
        }
    }

    /**
     * 初始化权重
     */
    private void initWeight() {
        this.weight = new double[layer - 1][][];
        for (int i = 0; i < layer - 1; i++) {
            this.weight[i] = new double[this.array[i]][];
            for (int j = 0; j < this.array[i]; j++) {
                this.weight[i][j] = new double[this.array[i + 1]];
                for (int k = 0; k < this.array[i + 1]; k++) {
                    this.weight[i][j][k] = RandomUtil.getRandom(0, 0.1);
                }
            }
        }
    }

    /**
     * 初始化网络残差
     */
    private void initDeta() {
        this.deta = new double[layer][];
        for (int i = 0; i < layer; i++) {
            this.deta[i] = new double[this.array[i]];
            for (int j = 0; j < this.array[i]; j++) {
                this.deta[i][j] = 0;
            }
        }
    }

    /**
     * 正向传播过程
     *
     * @param input
     * @return
     */
    private boolean forwardPropagating(double[] input) {
        // 初始化输入层
        List<Neuron> inputLayer = this.neuronList.get(0);
        if (inputLayer.size() != input.length) {
            System.err.println("[error] 输入层神经元和数字输入不同");
            return false;
        }
        for (int i = 0; i < inputLayer.size(); i++) {
            Neuron neuron = inputLayer.get(i);
            neuron.init(input[i], input[i]);
            neuron.sigmod();
        }

        for (int i = 1; i < layer; i++) {
            // 前向传播：从i-1层传播到i层
            List<Neuron> currentList = this.neuronList.get(i);
            List<Neuron> lastList = this.neuronList.get(i - 1);
            Neuron currentNeuron = null;
            Neuron lastNeuron = null;

            for (int j = 0; j < currentList.size(); j++) {
                double value = 0;
                currentNeuron = currentList.get(j);
                currentNeuron.setValue(0);
                for (int k = 0; k < lastList.size(); k++) {
                    lastNeuron = lastList.get(k);
                    value += lastNeuron.getOutput() * weight[i - 1][k][j];
                }
                currentNeuron.setValue(value);
                currentNeuron.sigmod();
            }
        }
        return true;
    }

    /**
     * 反向传播
     *
     * @param labels
     * @return
     */
    public boolean backardPropagating(int[] labels) {
        if (labels.length != this.neuronList.get(this.layer - 1).size()) {
            System.err.println("[error] 输出层神经元和数字表示层不同");
            return false;
        }

        // 初始化output层(layer-1)残差
        int m = this.neuronList.size() - 1;
        for (int i = 0; i < deta[m].length; i++) {
            double output = this.neuronList.get(m).get(i).getOutput();
            // 求导公式 deta(m)=(O(m)-Yi)*O(m)*(1-O(m))
            deta[m][i] = (output - labels[i]) * output * (1 - output);
        }
        for (int j = 0; j < this.neuronList.get(m - 1).size(); j++) {
            for (int i = 0; i < this.neuronList.get(m).size(); i++) {
                // 更新倒数第二层和最后一层之间的权重 W(ij) = -η*deta(m)*O(m)
                weight[m - 1][j][i] += (-1) * RATE * deta[m][i] * this.neuronList.get(m - 2).get(j).getOutput();
            }
        }

        for (int k = this.layer - 2; k > 0; k--) {
            for (int i = 0; i < this.neuronList.get(k).size(); i++) {
                double value = 0;
                for (int l = 0; l < this.neuronList.get(k + 1).size(); l++) {
                    //Σ(W(k)*deta(k+1))
                    value += (weight[k][i][l] * deta[k + 1][l]);
                }
                //deta(k)=(O(k)*(1-O(k))*(l)Σ(Wli*dl(k+1)))
                deta[k][i] = this.neuronList.get(k).get(i).getOutput() * (1 - this.neuronList.get(k).get(i).getOutput()) * value;
            }
            for (int j = 0; j < this.neuronList.get(k - 1).size(); j++) {
                for (int i = 0; i < this.neuronList.get(k).size(); i++) {
                    //ΔW(k-1) = -η*deta(k)*O(k-1)
                    weight[k - 1][j][i] += (-1) * RATE * deta[k][i] * this.neuronList.get(k - 1).get(j).getOutput();
                }
            }
        }
        return true;
    }

    /**
     * 获取输出层向量
     *
     * @return
     */
    public double[] getOutput() {
        double[] output = new double[this.neuronList.get(this.layer - 1).size()];
        for (int i = output.length - 1; i >= 0; i--)
            output[i] = this.neuronList.get(this.layer - 1).get(i).getOutput();
        return output;
    }

    /**
     * 误差判定
     *
     * @param labels
     * @return
     */
    private boolean getError(int[] labels) {
        boolean flag = true;
        List<Neuron> list = this.neuronList.get(this.layer - 1);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getOutput() == labels[i])
                flag = flag & true;
            else {
                if (list.get(i).getOutput() < labels[i])
                    flag = flag & ((list.get(i).getOutput() + STOP) > labels[i] ? true : false);
                else if (list.get(i).getOutput() > labels[i])
                    flag = flag & ((list.get(i).getOutput() - STOP) < labels[i] ? true : false);
            }
        }
        return flag;
    }

    /**
     * 训练数据
     *
     * @param features
     * @param labels
     */
    public void trainOne(double[] features, int[] labels) {
        for (int i = 0; i < NUMBER_ROUND; i++) {
            boolean flag = forwardPropagating(features);
            if (!flag) {
                return;
            }

            if (this.getError(labels))
                break;

            flag = backardPropagating(labels);
            if (!flag) {
                return;
            }
        }
    }

    /**
     * 测试数据
     *
     * @param input
     * @param label
     * @return
     */
    public boolean testOne(double input[], int[] label) {
        forwardPropagating(input);

        if (getError(label))
            return true;
        else
            return false;
    }

}
