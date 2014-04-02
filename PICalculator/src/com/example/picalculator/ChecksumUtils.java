package com.example.picalculator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtils {

	private static final int MAX = 0xff;
	private static final int BIT_8 = 0x100;
	private static final int LENGTH = 16;
	private static final int BUFFER_SIZE = 1024;
	private static final String TYPE = "MD5";

	public static String calculateMD5ChecksumForFile(String filename) throws FileNotFoundException,
			NoSuchAlgorithmException, IOException {
		InputStream fis = new FileInputStream(filename);
		byte[] buffer = new byte[BUFFER_SIZE];
		MessageDigest complete = MessageDigest.getInstance(TYPE);
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		byte[] b = complete.digest();
		String result = "";
		for (int i = 0; i < b.length; i++) { // casting to md5
			result += Integer.toString((b[i] & MAX) + BIT_8, LENGTH).substring(1);
		}
		return result;
	}
}