package core.com.wx.service.impl;

import core.com.chidori.service.WxPayService;
import core.com.chidori.utils.Utility;
import core.com.chidori.wxpay.IWxPayConfig;
import core.com.chidori.wxpay.JSSign;
import core.com.wx.model.JsApiTicketResponse;
import core.com.wx.service.WxTicketService;
import core.com.wx.service.WxTokenService;
import core.com.wx.service.http.HttpService;
import core.com.wx.utils.WxWebUrl;
import core.com.wxpay.WXPay;
import core.com.wxpay.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WxTicketServiceImpl extends AbstractWxService implements WxTicketService {
    private static final Logger logger = LoggerFactory.getLogger(WxTicketServiceImpl.class);

    private static ConcurrentHashMap<String, String> TICKET_MAP = new ConcurrentHashMap<>();
    private static final String JSAPI_TICKET = "jsapi_ticket";
    private static final String EXPIRES_IN = "expires_in";

    @Autowired
    private WxTokenService wxTokenService;

    @Autowired
    private HttpService httpService;

    @Autowired
    private IWxPayConfig iWxPayConfig;

    @Autowired
    private WxPayService wxPayService;

    @Override
    public String getJSAPITicket() {
        String JsApiTicket = null;

        if (TICKET_MAP.get(JSAPI_TICKET) != null && Integer.parseInt(TICKET_MAP.get(EXPIRES_IN)) > Utility.getCurrentTimeStamp()) {
            JsApiTicket = TICKET_MAP.get(JSAPI_TICKET);
            logger.info("getJSAPITicket() end, jsapi_ticket from redis, expires_in, accessToken={}", JsApiTicket);
            return JsApiTicket;
        }

        String accessToken = wxTokenService.getWxToken();

        // params
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(WxWebUrl.UrlParam.ACCESS_TOKEN, accessToken);
        JsApiTicketResponse response = null;
        try {
            response = httpService.get(this.fmtReqWebsUrl(WxWebUrl.GET_TICKET, paramMap), JsApiTicketResponse.class);
        } catch (Exception e) {
            logger.error("getJSAPITicket() exception. ", e);
        }

        if (response != null) {
            JsApiTicket = response.getTicket();
            Integer expiresIn = response.getExpires_in();
            TICKET_MAP.put(JSAPI_TICKET, JsApiTicket);
            TICKET_MAP.put(EXPIRES_IN, String.valueOf(expiresIn + Utility.getCurrentTimeStamp() - 600));
        }
        logger.info("getJSAPITicket() end, JsApiTicket={}", JsApiTicket);
        return JsApiTicket;
    }

    @Override
    public Map<String, String> getSdkConfig(String url) {
        logger.info("getSdkConfig() start, url={}", url);
        Map<String, String> result = new HashMap<>();

        result.putAll(JSSign.sign(this.getJSAPITicket(), url));
        result.put("appId", iWxPayConfig.getAppID());
        logger.info("getSdkConfig() end, result={}", result);
        return result;
    }

    @Override
    public String payBack(String notifyData) {
        logger.info("payBack() start, notifyData={}", notifyData);
        String xmlBack="";
        Map<String, String> notifyMap = null;
        try {
            WXPay wxpay = new WXPay(iWxPayConfig);

            notifyMap = WXPayUtil.xmlToMap(notifyData);         // 转换成map
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 签名正确
                // 进行处理。
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//订单号

                if (out_trade_no == null) {
                    logger.info("微信支付回调失败订单号: {}", notifyMap);
                    xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    return xmlBack;
                }

                wxPayService.payCallback(out_trade_no, return_code);
                logger.info("微信支付回调成功订单号: {}", notifyMap);
                xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[SUCCESS]]></return_msg>" + "</xml> ";
                return xmlBack;
            } else {
                logger.error("微信支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            logger.error("微信支付回调通知失败",e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }
}
