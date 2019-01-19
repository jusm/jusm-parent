package com.github.jusm;

public class RuntimeConstantPoolOOM {
	public static void main(String[] args) {
		String str1 = new StringBuilder("ja").append("va").toString();
		String str2 = new StringBuilder("clas").append("s").toString();
		System.out.println(str1.intern() == str1);
		System.out.println(str2.intern() == str2);
		System.out.println(str2.intern() == str2);
		
	}
}
