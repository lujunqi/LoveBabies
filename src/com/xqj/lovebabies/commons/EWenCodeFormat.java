package com.xqj.lovebabies.commons;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EWenCodeFormat {
	static String dataOne;
	/*
	 * 16���������ַ���
	 */
	private static String hexString = "0123456789ABCDEF";

	/*
	 * ���ַ��������16��������,�����������ַ����������ģ�
	 */
	public static String encode(String str) {
		dataOne = str;
		// ����Ĭ�ϱ����ȡ�ֽ�����
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// ���ֽ�������ÿ���ֽڲ���2λ16��������
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0) + " ");
		}

		return sb.toString();

	}
	
	//int to byte
	public static byte[] int2byte(int res) {
		byte[] targets = new byte[4];

		targets[0] = (byte) (res & 0xff);// ���λ 
		targets[1] = (byte) ((res >> 8) & 0xff);// �ε�λ 
		targets[2] = (byte) ((res >> 16) & 0xff);// �θ�λ 
		targets[3] = (byte) (res >>> 24);// ���λ,�޷������ơ� 
		return targets; 
		} 

	/*
	 * ��16�������ֽ�����ַ���,�����������ַ����������ģ�
	 */
	public static String decode(String bytes) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// ��ÿ2λ16����������װ��һ���ֽ�
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());

	}

	public static String StringFilter(String str) throws PatternSyntaxException {
		// ֻ������ĸ������
		// String regEx = "[^a-zA-Z0-9]";
		// ��������������ַ�
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~��@#��%����&*��������+|{}������������������������]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * ����* Convert byte[] to hex
	 * string.�������ǿ��Խ�byteת����int��Ȼ������Integer.toHexString(int)��ת����16�����ַ�����
	 * 
	 * ����* @param src byte[] data
	 * 
	 * ����* @return hex string
	 * 
	 * ����
	 */

	public static String bytesToHexString(byte[] src) {

		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {

			return null;

		}

		for (int i = 0; i < 20; i++) {

			int v = src[i] & 0xFF;

			String hv = Integer.toHexString(v);

			if (hv.length() < 2) {

				stringBuilder.append(0);
				System.out.println(stringBuilder);
			}

			stringBuilder.append(hv);

		}

		return stringBuilder.toString();

	}

	/** */
	/**
	 * ���ֽ�����ת����16�����ַ���
	 * 
	 * @param bArray
	 * @return
	 */
	public static final int[] bytesToHexStringTwo(byte[] bArray, int count) {
		int[] fs = new int[count];
		for (int i = 0; i < count; i++) {
			fs[i] = (0xFF & bArray[i]);
		}
		return fs;
	}

	// �ָ��ַ���
	public static String Stringspace(String str) {

		String temp = "";
		String temp2 = "";
		for (int i = 0; i < str.length(); i++) {

			if (i % 2 == 0) {
				temp = str.charAt(i) + "";
				temp2 += temp;
				System.out.println(temp);
			} else {
				temp2 += str.charAt(i) + " ";
			}

		}
		return temp2;
	}

	/**
	 * Byte -> Hex
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteToHex(byte[] bytes, int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex).append(" ");
		}
		return sb.toString();
	}

	/**
	 * String -> Hex
	 * 
	 * @param s
	 * @return
	 */
	public static String stringToHex(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			if (s4.length() == 1) {
				s4 = '0' + s4;
			}
			str = str + s4 + " ";
		}
		return str;
	}

}
