package com.github.jusm.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;


public class ProtoStuffUtil<T> {
	
	public static <T> byte[] serializate(T t,Class<T> clazz) {
		RuntimeSchema<T>  schema = RuntimeSchema.createFrom(clazz);
		byte[] byteArray = ProtostuffIOUtil.toByteArray(t, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
		return byteArray;
	} 
	
	
	public static <T> T deserializate(byte[] data,Class<T> clazz) {
		
		RuntimeSchema<T>  schema =  (RuntimeSchema<T>) RuntimeSchema.createFrom(clazz);
		T t = schema.newMessage();
		ProtostuffIOUtil.mergeFrom(data, t, schema);
		return t;
	}
	
}
