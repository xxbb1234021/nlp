package com.xb.services.hmm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xb.algoritm.hmm.ForwardAlgorithm;
import com.xb.algoritm.hmm.Viterbi;
import com.xb.bean.hmm.Hmm;
import com.xb.business.hmm.HmmAbstractFactory;
import com.xb.business.hmm.builderImpl.AbstractPinyingToHanziModel;
import com.xb.business.hmm.factoryImpl.PinyingToHanziFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HmmService {
    private static Logger LOGGER = LoggerFactory.getLogger(HmmService.class);

    public double forward(Hmm h) {
        return ForwardAlgorithm.forward(h);
    }

    /**
     * 求解HMM模型
     *
     * @param h HMM模型
     * @return
     */
    public Integer[] caculateHmmResult(Hmm h) {
        return Viterbi.getMaxProbGroup(h);
    }

    public String getHanzi(String splitSpell) {
        //String splitPinYin = MaxMatchingPinYinSegmenter.getInstance(Constant.PINYIN_TRIE_TREE).segment(splitSpell);
        //LOGGER.info("###################"+splitPinYin);

        //		PinyinTokenizer p = new PinyinTokenizer("D:\\workspace\\nlp\\src\\main\\resources\\pinyin\\pinyin2.utf8");
        //		String[] pinyin = p.tokenize(splitSpell);
        HmmAbstractFactory factory = new PinyingToHanziFactory();
        AbstractPinyingToHanziModel builder = factory.createPinyingToHanziModelBuilder2();

        Hmm h = new Hmm();
        Map<String, Integer> pinyingPositionMap = builder.getPinyinPositionMap();
        //String[] pinyin = splitSpell.split("\\|");
        String[] pinyin = splitSpell.split(" ");
        int[] obs = new int[pinyin.length];
        for (int i = 0; i < pinyin.length; i++) {
            if (StringUtils.isBlank(pinyin[i])) {
                continue;
            }
            obs[i] = pinyingPositionMap.get(pinyin[i]) == null ? 1 : pinyingPositionMap.get(pinyin[i]);
        }

        h.setObs(obs);

        int[] states = new int[builder.getWordNum()];
        for (int i = 0; i < builder.getWordNum(); i++) {
            states[i] = i;
        }
        h.setStates(states);
        h.setStartProb(builder.getPrioriProbability());
        h.setTransProb(builder.getTransformProbability());
        h.setEmitProb(builder.getEmissionProbability());

        StringBuilder sb = new StringBuilder();
        List<List<Integer>> allGroup = Viterbi.getAllProbGroup(h);
        List<Integer> seqList = null;
        Object[] obj = new Object[allGroup.size()];
        for (int i = 0, size = allGroup.size(); i < size; i++) {
            seqList = allGroup.get(i);
            for (int j = 0, seqSize = seqList.size(); j < seqSize; j++){
                sb.append(builder.getDiffWord()[seqList.get(j)]);
            }
            obj[i] = sb.toString();

            sb.delete(0, sb.length());
        }
        JSON json = (JSON) JSONObject.toJSON(obj);

        return json.toString();
    }
}
