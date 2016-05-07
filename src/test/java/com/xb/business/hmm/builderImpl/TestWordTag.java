package com.xb.business.hmm.builderImpl;

import com.xb.business.hmm.Director;
import com.xb.bean.hmm.Hmm;
import com.xb.business.hmm.HmmAbstractFactory;
import com.xb.business.hmm.factoryImpl.WordTaggingFactory;
import com.xb.constant.Constant;
import com.xb.constant.WordTaggingConstant;
import com.xb.services.hmm.HmmService;
import com.xb.algoritm.segment.WordSegmenter;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2015/12/31.
 */
public class TestWordTag {

    @Test
    public void testViterbi() {
        // TODO Auto-generated method stub
        HmmService hs = new HmmService();
        int observeLenght = 3;
        int[] observeSequence = {0, 1, 0};

        int[] states = new int[]{0, 1, 2};
        double[][] a = {{0.500, 0.375, 0.125}, {0.250, 0.125, 0.625}, {0.250, 0.375, 0.375}};
        double[][] b = {{0.60, 0.20, 0.15, 0.05}, {0.25, 0.25, 0.25, 0.25}, {0.05, 0.10, 0.35, 0.50}};
        double[] pi = {0.63, 0.17, 0.20};

        Hmm h = new Hmm();
        //h.setObs(observeSequence);
        //h.setStates(states);
        //h.setStartProb(pi);
        //h.setTransProb(a);
        //h.setEmitProb(b);
        //hs.forward(h);

        states = new int[]{0, 1};
        int[] observations = new int[]{0, 1, 2};
        double[] start_probability = new double[]{0.6, 0.4};
        double[][] transititon_probability = new double[][]{{0.7, 0.3}, {0.4, 0.6},};
        double[][] emission_probability = new double[][]{{0.1, 0.4, 0.5}, {0.6, 0.3, 0.1},};

        //        states = new int[]{0, 1, 2};
        //        int[] observations = new int[]{0, 1, 2};
        //        double[] start_probability = new double[]{0.63, 0.17, 0.2};
        //        double[][] transititon_probability = new double[][]{
        //                {0.5, 0.375, 0.125},
        //                {0.25, 0.125, 0.625},
        //                {0.25, 0.375, 0.375}
        //        };
        //        double[][] emission_probability = new double[][]{
        //                {0.6, 0.2, 0.05},
        //                {0.25, 0.25, 0.25},
        //                {0.05, 0.10, 0.50}
        //        };

        //        h = new Hmm();
        //        h.setObs(observations);
        //        h.setStates(states);
        //        h.setStartProb(start_probability);
        //        h.setTransProb(transititon_probability);
        //        h.setEmitProb(emission_probability);


        String source = "咬死了猎人的狗";
        WordSegmenter mmsegger = new WordSegmenter(Constant.WORD_TRIE_TREE);
        String splitWrod = mmsegger.segment(source);
        //System.out.println(splitWrod);
        String[] words = splitWrod.split("\\|");
        List<String> wordList = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            wordList.add(words[i]);
        }

        HmmAbstractFactory factory = new WordTaggingFactory();
        AbstractWordTagginModel builder = factory.createWordTagginModelBuilder();
        Director director = new Director(builder);
        director.constructHmmModel();

        //List<String> smallArrayList = builder.smallSeg(wordList);
        List<String> smallArrayList = wordList;
        String[] example = new String[smallArrayList.size()];
        for (int i = 0; i < example.length; i++) {
            example[i] = smallArrayList.get(i);
        }

        h = new Hmm();
        int[] obs = new int[example.length];
        for (int i = 0; i < example.length; i++) {
            obs[i] = builder.getWordPositionMap().get(example[i]) == null ? 1 : builder.getWordPositionMap().get(example[i]);
        }

        h.setObs(obs);

        states = new int[builder.getWordTagNum()];
        for (int i = 0; i < builder.getWordTagNum(); i++) {
            states[i] = i;
        }
        h.setStates(states);
        h.setStartProb(builder.getPrioriProbability());
        h.setTransProb(builder.getTransformProbability());
        h.setEmitProb(builder.getEmissionProbability());

        Integer[] result = hs.caculateHmmResult(h);
        for (int r : result) {
            System.out.print(r + " ");
        }

        StringBuilder sentence = new StringBuilder();
        String wd = "";
        for (int i = 0; i < example.length; i++) {
            wd = builder.getDiffWordTag()[result[i]];
            sentence.append(example[i]).append("|").append(wd).append(" ");
            example[i] = example[i].concat("(" + wd + "--" + WordTaggingConstant.WORD_TAGGING_MAP.get(wd) + ")");
            //example[i] = example[i].concat("(" + wd + ")");
            //index = previous[i][index];
        }
        System.out.println();
        for (String r : example) {
            System.out.print(r + " ");
        }

        System.out.println();
        System.out.println(sentence.toString());
    }
}
