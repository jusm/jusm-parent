package com.weshare.chidori.impl;

import com.weshare.chidori.TestBase;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class OrderServiceImplTest extends TestBase {
    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImplTest.class);

//    @Autowired
//    private IWxPayConfig iWxPayConfig;

    @Test
    public void orderTest(){
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String orderId= 1 + String.format("%015d", hashCodeV);
    }

    @Test
    public void doPay() {

        String str = "A\uD83D\uDC97恋家披萨\uD83D\uDC97A";
        //进行编码
        String nickname = null;
        nickname = Base64.encodeBase64String(str.getBytes(StandardCharsets.UTF_8));

        System.out.println(nickname);
        //进行解码
        nickname = new String(Base64.decodeBase64(nickname), StandardCharsets.UTF_8);
        System.out.println(nickname);

//        logger.info("doPay start");
//        logger.info(iWxPayConfig.getAppID());
//        logger.info(iWxPayConfig.getKey());
//        logger.info(iWxPayConfig.getMchID());
//
//        try {
//            WXPay wxpay = new WXPay(iWxPayConfig);
//            Map<String, String> data = new HashMap<String, String>();
//            data.put("body", "腾讯充值中心-QQ会员充值");
//            data.put("out_trade_no", "20150806125346");
//            data.put("total_fee", "1");
//            data.put("spbill_create_ip", "192.168.31.166");
//            data.put("openid", "oPg6Q0TE3VitYTroCpxnA89ZBF5M");
//            data.put("notify_url", "http://lianjia.oopmind.com");
//            data.put("trade_type", "JSAPI");
//
//            Map<String, String> resp = wxpay.unifiedOrder(data);
//            String reqBody =WXPayUtil.mapToXml(resp);
//            logger.info(reqBody);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
