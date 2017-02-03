package com.xb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConsoleReader {
    private static Logger LOGGER = LoggerFactory.getLogger(ConsoleReader.class);

    private BufferedReader reader;

    public ConsoleReader(InputStream is) {
        reader = new BufferedReader(new InputStreamReader(is));
    }

    public int readInt() throws IOException, NumberFormatException {
        int returnInt = 0;
        String s;
        try {
            s = reader.readLine();
            returnInt = Integer.parseInt(s);
            if (s.length() == 0)
                throw new NumberFormatException();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return returnInt;
    }

    public double readDouble() throws IOException, NumberFormatException {
        double returnDouble = 0;
        String s;
        try {
            s = reader.readLine();
            returnDouble = Double.parseDouble(s);
            if (s.length() == 0)
                throw new NumberFormatException();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return returnDouble;
    }

    public String readLine() {
        String inputLine = "";
        try {
            inputLine = reader.readLine();
        } catch (Exception e) {
            LOGGER.error("", e);
        }
        return inputLine;
    }
}
