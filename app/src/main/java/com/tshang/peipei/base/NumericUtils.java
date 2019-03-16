package com.tshang.peipei.base;

public final class NumericUtils {
	public static int parseInt(String string, int defaultValue) {
		int value;

		try {
			value = Integer.parseInt(string);
		} catch (Exception e) {
			value = defaultValue;
		}

		return value;
	}

	public static long parseLong(String string, int defaultValue) {
		long value;

		try {
			value = Long.parseLong(string);
		} catch (Exception e) {
			value = defaultValue;
		}

		return value;
	}
	
	public static long parseLong(String string, long defaultValue) {
		long value;

		try {
			value = Long.parseLong(string);
		} catch (Exception e) {
			value = defaultValue;
		}

		return value;
	}
	
	public static double parseDouble(String string, double defaultValue) {
		double value;

		try {
			value = Double.parseDouble(string);
		} catch (Exception e) {
			value = defaultValue;
		}

		return value;
	}
	
	public static float parseFloat(String string, float defaultValue) {
		float value;

		try {
			value = Float.parseFloat(string);
		} catch (Exception e) {
			value = defaultValue;
		}

		return value;
	}
	
}
