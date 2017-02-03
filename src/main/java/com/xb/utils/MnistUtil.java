package com.xb.utils;

import com.xb.bean.mnist.MnistImage;
import com.xb.constant.Constant;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kevin on 2017/1/10 0010.
 */
public class MnistUtil {
    //  http://yann.lecun.com/exdb/mnist/

    private static final int MAGIC_OFFSET = 0;
    private static final int OFFSET_SIZE = 4; //in bytes

    private static final int LABEL_MAGIC = 2049;
    private static final int IMAGE_MAGIC = 2051;

    private static final int NUMBER_ITEMS_OFFSET = 4;
    private static final int ITEMS_SIZE = 4;

    private static final int NUMBER_OF_ROWS_OFFSET = 8;
    private static final int ROWS_SIZE = 4;
    public static final int ROWS = 28;

    private static final int NUMBER_OF_COLUMNS_OFFSET = 12;
    private static final int COLUMNS_SIZE = 4;
    public static final int COLUMNS = 28;

    private static final int IMAGE_OFFSET = 16;
    private static final int IMAGE_SIZE = ROWS * COLUMNS;

    private static final int[][] lables = {
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    };

    public static List<MnistImage> loadMnistImages(String labelFileName, String imageFileName) throws IOException {
        List<MnistImage> images = new ArrayList<MnistImage>();

        byte[] labelBytes = FileNIOUtil.readByteFile(labelFileName, Constant.CHARSET_UTF8);
        byte[] imageBytes = FileNIOUtil.readByteFile(imageFileName, Constant.CHARSET_UTF8);

        byte[] labelMagic = Arrays.copyOfRange(labelBytes, MAGIC_OFFSET, OFFSET_SIZE);
        byte[] imageMagic = Arrays.copyOfRange(imageBytes, MAGIC_OFFSET, OFFSET_SIZE);

        if (ByteBuffer.wrap(labelMagic).getInt() != LABEL_MAGIC) {
            throw new IOException("Bad magic number in label file!");
        }

        if (ByteBuffer.wrap(imageMagic).getInt() != IMAGE_MAGIC) {
            throw new IOException("Bad magic number in image file!");
        }

        int numberOfLabels = ByteBuffer.wrap(Arrays.copyOfRange(labelBytes, NUMBER_ITEMS_OFFSET, NUMBER_ITEMS_OFFSET + ITEMS_SIZE)).getInt();
        int numberOfImages = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUMBER_ITEMS_OFFSET, NUMBER_ITEMS_OFFSET + ITEMS_SIZE)).getInt();

        if (numberOfImages != numberOfLabels) {
            throw new IOException("The number of labels and images do not match!");
        }

        int numRows = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUMBER_OF_ROWS_OFFSET, NUMBER_OF_ROWS_OFFSET + ROWS_SIZE)).getInt();
        int numCols = ByteBuffer.wrap(Arrays.copyOfRange(imageBytes, NUMBER_OF_COLUMNS_OFFSET, NUMBER_OF_COLUMNS_OFFSET + COLUMNS_SIZE)).getInt();

        if (numRows != ROWS && numCols != COLUMNS) {
            throw new IOException("Bad image. Rows and columns do not equal " + ROWS + "x" + COLUMNS);
        }

        int num = 0;
        byte[] imageData = null;
        double[] imageData2Double = null;
        for (int i = 0; i < numberOfLabels; i++) {
            num = labelBytes[OFFSET_SIZE + ITEMS_SIZE + i];
            imageData = Arrays.copyOfRange(imageBytes, (i * IMAGE_SIZE) + IMAGE_OFFSET, (i * IMAGE_SIZE) + IMAGE_OFFSET + IMAGE_SIZE);
            imageData2Double = new double[imageData.length];
            for (int j = 0; j < imageData.length; j++) {
                imageData2Double[j] = imageData[j] & 0x0FF;
            }
            images.add(new MnistImage(num, lables[num], imageData, imageData2Double));
        }
        return images;
    }
}
