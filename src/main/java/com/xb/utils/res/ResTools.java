package com.xb.utils.res;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 2016/2/24.
 */
public class ResTools {
	private static Logger LOGGER = Logger.getLogger(ResTools.class);

	private static final Map<String, String> conf = new HashMap<String, String>();

	public static void set(String key, String value) {
		conf.put(key, value);
	}

	public static String get(String key, String defaultValue) {
		String value = conf.get(key) == null ? defaultValue : conf.get(key);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("获取配置项：" + key + "=" + value);
		}
		return value;
	}
}
