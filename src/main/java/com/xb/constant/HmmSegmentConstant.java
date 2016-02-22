package com.xb.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2016/1/21.
 */
public class HmmSegmentConstant {

    public static final Map<Integer, String> HMM_SEGMENT_MAP = initHmmSegmentMap();

    public static final Map<Integer, String> initHmmSegmentMap() {
        Map<Integer, String> hmmSegmentMap = new HashMap<Integer, String>();
        hmmSegmentMap.put(0, "B");
        hmmSegmentMap.put(1, "M");
        hmmSegmentMap.put(2, "E");
        hmmSegmentMap.put(3, "S");

        return hmmSegmentMap;
    }
}
