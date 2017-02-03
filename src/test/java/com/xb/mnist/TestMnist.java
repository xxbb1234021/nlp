package com.xb.mnist;

import com.xb.algoritm.neuralnet.BPNeuralNetList;
import com.xb.bean.mnist.MnistImage;
import com.xb.constant.FileConstant;
import com.xb.utils.MnistUtil;
import org.junit.Test;

import java.util.List;

public class TestMnist {

    @Test
    public void testMnist() throws Exception {
        List<MnistImage> trainImages = MnistUtil.loadMnistImages(FileConstant.TRAIN_LABELS_IDX1_UBYTE, FileConstant.TRAIN_IMAGES_IDX3_UBYTE);
        List<MnistImage> testImages = MnistUtil.loadMnistImages(FileConstant.T10K_LABELS_IDX1_UBYTE, FileConstant.T10K_IMAGES_IDX3_UBYTE);

        BPNeuralNetList bp = new BPNeuralNetList(new int[]{784, 20, 10});

        bp.train(trainImages);

        bp.test(testImages);
    }
}