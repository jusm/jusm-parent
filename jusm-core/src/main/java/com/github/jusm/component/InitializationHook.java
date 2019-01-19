package com.github.jusm.component;

/**
 * 
 * 系统 初始化 钩子接口
 */
public interface InitializationHook {

	/**
	 * 初始化系统 的钩子函数 如果业务有需要在系统初始化的时候实现此接口
	 * 
	 * @return
	 */
	boolean initialize();
	
	
	String description();
}
