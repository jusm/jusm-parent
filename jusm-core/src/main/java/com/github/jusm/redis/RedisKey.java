package com.github.jusm.redis;

public enum RedisKey {

	SEQUENCE_ORDER_ID("USM:SEQUENCE:ORDER_ID", "订单号增长");

	private String key;

	private String desc;

	private RedisKey(String key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	public String getKey() {
		return key;
	}

	public String getKey(String... strings) {
		StringBuilder sb = new StringBuilder(key);
		if (strings != null && strings.length > 0) {
			sb.append(":");
			int j = strings.length - 1;
			for (int i = 0; i < j; i++) {
				String string = strings[i];
				sb.append(string);
			}
			sb.append(strings[j]);
		}
		return sb.toString();
	}

	public String getDesc() {
		return desc;
	}

}
