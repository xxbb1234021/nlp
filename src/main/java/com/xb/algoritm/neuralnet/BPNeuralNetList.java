package com.xb.algoritm.neuralnet;

import com.xb.bean.mnist.MnistImage;

import java.util.List;

/**
 * Created by kevin on 2017/1/18 0018.
 */
public class BPNeuralNetList {
    private BPNeuralNet[] bpNeuralNet;

    public BPNeuralNetList(int nodeNumOfLayer[]) {
        bpNeuralNet = new BPNeuralNet[nodeNumOfLayer[nodeNumOfLayer.length - 1]];
        for (int i = 0; i < bpNeuralNet.length; i++)
            bpNeuralNet[i] = new BPNeuralNet(nodeNumOfLayer);
    }

    public void train(List<MnistImage> images) {
        MnistImage mnistImage = null;
        int num = images.size();
        long t = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            mnistImage = images.get(i);
            this.bpNeuralNet[mnistImage.getNum()].trainOne(mnistImage.getImdageDataDouble(), mnistImage.getLabels());
            System.out.println((i + 1) + " .train percent:" + (((double) i) / ((double) num)) * 100 + " %.");
        }
        System.out.println("train finish!");
        System.out.println("Running time: " + (System.currentTimeMillis() - t) + "ms");
    }

    public void test(List<MnistImage> testImages) {
        int right = 0, sum = 0;
        MnistImage mnistImage = null;
        for (int i = 0; i < testImages.size(); i++) {
            mnistImage = testImages.get(i);
            for (int j = 0; j < getBpNeuralNet().length; j++) {
                boolean flag = false;
                if (flag = getBpNeuralNet()[j].testOne(mnistImage.getImdageDataDouble(), mnistImage.getLabels())) {
                    System.out.print(j + " " + flag + " " + mnistImage.getNum() + " ");
                    if (j == mnistImage.getNum())
                        right++;
                    break;
                }
            }
            sum++;
            System.out.println((i + 1) + ". Correct percent: " + (((double) right) / ((double) sum)) * 100 + " %.");
        }
    }


    public BPNeuralNet[] getBpNeuralNet() {
        return bpNeuralNet;
    }
}
