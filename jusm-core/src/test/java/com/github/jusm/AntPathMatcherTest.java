package com.github.jusm;

import org.springframework.util.AntPathMatcher;

public class AntPathMatcherTest {
	private final static AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	public static void main(String[] args) {
		System.out.println(antPathMatcher.match("/order/*/detail", "/order/12/detail"));
		System.out.println(antPathMatcher.match("/order/*/detail", "/order/ 1dss12/detail"));
		System.out.println(antPathMatcher.match("/order/*/detail", "/order/12 sad/detail"));
		System.out.println(antPathMatcher.match("/order/*/detail", "/order/12aaaaaaaa/detail"));
		System.out.println(antPathMatcher.match("/order/*/detail", "/order/sdasdas/detail"));
		System.out.println(antPathMatcher.match("/order/*/detail", "/order/sdasdas/detail"));
		System.out.println(antPathMatcher.match("/order/*/detail", "/order/sdasdas/detail"));
		System.out.println(antPathMatcher.match("/order/*/detail", "/order/sdas/das/detail"));
		System.out.println(antPathMatcher.match("/order/**/detail", "/order/sdas/das/detail"));
		System.out.println(antPathMatcher.match("/login?l=**", "/login?l=zh-CN"));
	}
	
}
