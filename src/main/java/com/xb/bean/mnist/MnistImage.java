package com.xb.bean.mnist;

public class MnistImage {

    private int num;
    private int[] labels;
    private byte[] imageData;
    private double[] imdageDataDouble;

    public MnistImage(int num, int[] labels, byte[] imageData, double[] imdageDataDouble) {
        this.num = num;
        this.labels = labels;
        this.imageData = imageData;
        this.imdageDataDouble = imdageDataDouble;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public int[] getLabels() {
        return labels;
    }

    public void setLabels(int[] labels) {
        this.labels = labels;
    }

    public double[] getImdageDataDouble() {
        return imdageDataDouble;
    }

    public void setImdageDataDouble(double[] imdageDataDouble) {
        this.imdageDataDouble = imdageDataDouble;
    }
}
