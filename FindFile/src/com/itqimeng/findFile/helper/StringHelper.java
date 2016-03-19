package com.itqimeng.findFile.helper;

public class StringHelper {

	public static boolean isBlank(String input){
		boolean result = true;
		if(input != null && !"".equals(input.trim())){
			result = false;
		}
		return result;
	}
	
	public static boolean isNotBlank(String input){
		return !isBlank(input);
	}
}
