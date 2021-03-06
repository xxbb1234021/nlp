package com.xb.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileNIOUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(FileNIOUtil.class);

    private static final int BUFFER_AREA = 2 * 1024;

    public static void writeFile(String filePath, String context, String charCode) {
        byte[] b = null;
        FileOutputStream fos = null;
        FileChannel fc = null;
        File file = null;
        try {
            Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(filePath);
            while (ps.hasMoreElements()) {
                URL url = ps.nextElement();
                LOGGER.info("类路径资源URL：" + url);
                b = context.getBytes(charCode);
                file = new File(url.getFile());
                fos = new FileOutputStream(file.getAbsoluteFile(), true);
                fc = fos.getChannel();
                ByteBuffer bb = ByteBuffer.allocate(b.length);
                bb.put(b);
                bb.flip();
                fc.write(bb);
                fc.close();
                fos.close();
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    public static List<String> dirList(String filePath) {
        List<String> fileList = new ArrayList<String>();
        try {
            Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(filePath);
            while (ps.hasMoreElements()) {
                URL url = ps.nextElement();
                LOGGER.info("类路径资源URL：" + url);
                File file = new File(url.getFile());
                Path dir = Paths.get(file.getAbsolutePath());
                DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
                for (Path path : stream) {
                    if (Files.isDirectory(path)) {
                        LOGGER.info("Directory:" + path.getFileName());
                        dirList(path.toString());
                    } else {
                        LOGGER.info("File:" + path.getFileName());
                        fileList.add(filePath + "/" + path.getFileName());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return fileList;
    }

    public static String readFile(String filePath, String charCode) {
        StringBuilder sb = new StringBuilder();
        List<String> lines = readFileLine(filePath, charCode);
        for (int i = 0; i < lines.size(); i++) {
            sb.append(lines.get(i));
        }
        return sb.toString();
    }

    public static byte[] readByteFile(String filePath, String charCode) {
        byte[] data = null;

        try {
            Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(filePath);
            while (ps.hasMoreElements()) {
                URL url = ps.nextElement();
                LOGGER.info("类路径资源URL：" + url);
                File file = new File(url.getFile());
                data = Files.readAllBytes(Paths.get(URLDecoder.decode(file.getAbsolutePath(), charCode)));
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return data;
    }

    public static List<String> readFileLine(String filePath, String charCode) {
        try {
            Enumeration<URL> ps = FileNIOUtil.class.getClassLoader().getResources(filePath);
            while (ps.hasMoreElements()) {
                URL url = ps.nextElement();
                LOGGER.info("类路径资源URL：" + url);
                File file = new File(url.getFile());
                return Files.readAllLines(Paths.get(URLDecoder.decode(file.getAbsolutePath(), charCode)), Charset.forName(charCode));
            }
        } catch (IOException e) {
            LOGGER.error("", e);
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        writeFile("d:\\DEMO3.JSON", "fjdskfjsl发打发打发士大夫", "utf-8");
    }
}