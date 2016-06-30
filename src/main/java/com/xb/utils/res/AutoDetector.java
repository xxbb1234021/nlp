package com.xb.utils.res;

import static java.lang.System.currentTimeMillis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kevin on 2016/2/24.
 */
public class AutoDetector {
	private static Logger LOGGER = LoggerFactory.getLogger(AutoDetector.class);

	/**
	 * 加载资源
	 * @param resourceLoader 资源加载逻辑
	 * @param resourcePaths 多个资源路径，用逗号分隔
	 * @param charset 字符
	 */
	public static void loadRes(ResourceLoader resourceLoader, String resourcePaths, String charset) {
		resourcePaths = resourcePaths.trim();
		if ("".equals(resourcePaths)) {
			LOGGER.info("没有资源可以加载");
			return;
		}
		LOGGER.info("开始加载资源");
		LOGGER.info(resourcePaths);
		long start = currentTimeMillis();
		List<String> result = new ArrayList<String>();
		for (String resource : resourcePaths.split("[,，]")) {
			try {
				resource = resource.trim();
				if (resource.startsWith("classpath:")) {
					//处理类路径资源
					result.addAll(loadClasspathResource(resource.replace("classpath:", ""), charset));
				}
			} catch (Exception e) {
				LOGGER.error("加载资源失败：" + resource, e);
			}
		}
		LOGGER.info("加载资源 " + result.size() + " 行");
		//调用自定义加载逻辑
		resourceLoader.clear();
		resourceLoader.load(result);
		long cost = currentTimeMillis() - start;
		LOGGER.info("完成加载资源，耗时" + cost + " 毫秒");
	}

	/**
	 * 加载类路径资源
	 * @param resource 资源名称
	 * @param charset 字符
	 * @return 资源内容
	 * @throws IOException
	 */
	private static List<String> loadClasspathResource(String resource, String charset) throws IOException {
		List<String> result = new ArrayList<String>();
		LOGGER.info("类路径资源：" + resource);
		Enumeration<URL> ps = AutoDetector.class.getClassLoader().getResources(resource);
		while (ps.hasMoreElements()) {
			URL url = ps.nextElement();
			LOGGER.info("类路径资源URL：" + url);

			File file = new File(url.getFile());
			result.addAll(load(file.getAbsolutePath(), charset));
		}
		return result;
	}

	/**
	 * 加载文件资源
	 * @param path 文件路径
	 * @param charset 字符
	 * @return 文件内容
	 */
	private static List<String> load(String path, String charset) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(path), Charset.forName(charset));
			return lines;
		} catch (Exception e) {
			LOGGER.error("加载资源失败：" + path, e);
		}
		return null;
	}
}
