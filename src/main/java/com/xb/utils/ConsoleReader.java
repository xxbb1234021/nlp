package com.xb.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConsoleReader {
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
		} catch (IOException IO) {
			System.err.println("InputException!");
			System.err.println();
			System.exit(1);
		} catch (NumberFormatException NF) {
			System.err.println("NumberFormatException!");
			System.err.println("Notinputempty!");
			System.err.println("PleaseinputIntegernumber!");
			System.err.println();
			System.exit(1);
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
		} catch (IOException IO) {
			System.err.println("Input/OutputException!");
			System.err.println();
			System.exit(1);
		} catch (NumberFormatException NF) {
			System.err.println("NumberFormatException!");
			System.err.println("Notinputempty!");
			System.err.println("PleaseinputDoublenumber!");
			System.err.println();
			System.exit(1);
		}
		return returnDouble;
	}

	public String readLine() {
		String inputLine = "";
		try {
			inputLine = reader.readLine();
		} catch (IOException e) {
			System.err.println("Input/OutputException!");
			System.err.println();
			System.exit(1);
		} catch (NumberFormatException NF) {
			System.err.println("NumberFormatException!");
			System.err.println("Notinputempty!");
			System.err.println("PleaseinputString!");
			System.err.println();
			System.exit(1);
		}
		return inputLine;
	}
}
