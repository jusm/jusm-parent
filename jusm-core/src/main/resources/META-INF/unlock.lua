if redis.call("get",KEYS[1] == ARGV[1] THEN 
	return redis.call("del",KEYS[1]))
else
	return 0
end