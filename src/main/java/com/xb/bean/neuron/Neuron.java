package com.xb.bean.neuron;

/**
 * 神经元
 */
public class Neuron {

    /**
     * 神经元值
     */
    public double value;
    /**
     * 神经元输出值
     */
    public double output;

    public Neuron() {
        init();
    }

    public Neuron(double value) {
        init(value);
    }

    public Neuron(double value, double output) {
        this.value = value;
        this.output = output;
    }

    public void init() {
        this.value = 0;
        this.output = 0;
    }

    public void init(double value) {
        this.value = value;
        this.output = 0;
    }

    public void init(double value, double output) {
        this.value = value;
        this.output = output;
    }

    /**
     * sigmod激活函数
     */
    public void sigmod() {
        this.output = 1.0 / (1.0 + Math.exp(-1.0 * this.value));
    }

    public String toString() {
        return "(" + value + " " + output + ")";
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }
}