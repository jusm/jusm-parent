package core.com.chidori.service;

import core.com.chidori.model.TransOrder;
import core.com.chidori.model.info.DoPayResponse;

import java.util.Map;

public interface WxPayService {

    /**
     * 微信下单接口
     */
    DoPayResponse doPay();

    Map<String, String> doTransOrder(TransOrder transOrder, String openId);

    void payCallback(String globalOrderGid, String returnCode);

    String orderQuery(String orderGid);

}
