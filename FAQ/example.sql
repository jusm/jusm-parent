SELECT CONCAT (
'*10\r\n',

'$',LENGTH(redis_cmd),'\r\n',redis_cmd,'\r\n',
'$',LENGTH(redis_key),'\r\n',redis_key,'\r\n',

'$',LENGTH(hkey1),'\r\n',hkey1,'\r\n',
'$',LENGTH(hval1),'\r\n',hval1,'\r\n',

'$',LENGTH(hkey2),'\r\n',hkey2,'\r\n',
'$',LENGTH(hval2),'\r\n',hval2,'\r\n',

'$',LENGTH(hkey3),'\r\n',hkey3,'\r\n',
'$',LENGTH(hval3),'\r\n',hval3,'\r\n',
 
'$',LENGTH(hkey4),'\r\n',hkey4,'\r\n',
'$',LENGTH(hval4),'\r\n',hval4,'\r'
) AS resp
FROM (
SELECT 
'HSET' AS redis_cmd,
CONCAT('user:info:',openid) AS redis_key,
'openid' AS hkey1,openid AS hval1,
'userid' AS hkey2,openid AS hval2,
'readlname' AS hkey3,openid AS hval3,
'score' AS hkey4,openid AS hval4

FROM biz_account_balance
) AS t