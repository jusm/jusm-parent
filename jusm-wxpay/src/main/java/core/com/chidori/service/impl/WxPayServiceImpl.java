package core.com.chidori.service.impl;

import core.com.chidori.model.TransOrder;
import core.com.chidori.model.common.ChidoriException;
import core.com.chidori.model.info.DoPayResponse;
import core.com.chidori.service.WxPayService;
import core.com.chidori.utils.ErrorCode;
import core.com.chidori.utils.GlobalConstants;
import core.com.chidori.utils.Utility;
import core.com.chidori.wxpay.IWxPayConfig;
import core.com.wxpay.WXPay;
import core.com.wxpay.WXPayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {
    private final static Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);

    @Override
    public DoPayResponse doPay() {

        String openId = ""; // 微信下单的用户 openid
        TransOrder transOrder = new TransOrder(); // 订单基本信息

        Map<String, String > resultMap = this.doTransOrder(transOrder, openId);
        DoPayResponse response = new DoPayResponse();
        response.setResultMap(resultMap);
        return response;
    }

    private final String SUCCESS = "SUCCESS";

    @Autowired
    private IWxPayConfig iWxPayConfig;

    @Override
    public Map<String, String>  doTransOrder(TransOrder transOrder, String openId) {
        logger.info("doTransOrder start, transOrder={}", transOrder);
        if (transOrder == null || openId == null) {
            throw new ChidoriException(ErrorCode.SYS_PARAMETER_ERROR);
        }
        // 发起微信支付
        WXPay wxpay = null;
        Map<String, String> result = new HashMap<>();
        try {
            wxpay = new WXPay(iWxPayConfig);

            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "订单基本信息");
            data.put("out_trade_no", transOrder.getGlobalOrderId());
            data.put("total_fee", String.valueOf(transOrder.getOrderAmount().multiply(new BigDecimal(100)).intValue()));
            data.put("spbill_create_ip", "192.168.31.166");
            data.put("openid", openId);
            data.put("notify_url", "https://***/payCallback"); // 订单回调接口
            data.put("trade_type", "JSAPI");
            data.put("time_expire", "20181211110503");

            logger.info("发起微信支付下单接口, request={}", data);
            Map<String, String> response = wxpay.unifiedOrder(data);
            logger.info("微信支付下单成功, 返回值 response={}", response);
            String returnCode = response.get("return_code");
            if (!SUCCESS.equals(returnCode)) {
                return null;
            }
            String resultCode = response.get("result_code");
            if (!SUCCESS.equals(resultCode)) {
                return null;
            }
            String prepay_id = response.get("prepay_id");
            if (prepay_id == null) {
                return null;
            }

            // TODO: 2018/12/10 业务处理(保存微信下单结果)

            String packages = "prepay_id=" + prepay_id;
            Map<String, String> wxPayMap = new HashMap<String, String>();
            wxPayMap.put("appId", iWxPayConfig.getAppID());
            wxPayMap.put("timeStamp", String.valueOf(Utility.getCurrentTimeStamp()));
            wxPayMap.put("nonceStr", Utility.generateUUID());
            wxPayMap.put("package", packages);
            wxPayMap.put("signType", "MD5");
            String sign = WXPayUtil.generateSignature(wxPayMap, iWxPayConfig.getKey());

            result.put("prepay_id", prepay_id);
            result.put("sign", sign);
            result.putAll(wxPayMap);
            return result;
        } catch (Exception e) {
            logger.error("doTransOrder error", e);
        }
        return null;
    }

    @Override
    public void payCallback(String globalOrderGid, String returnCode) {
        // TODO: 2018/12/10 callback 处理，业务逻辑处理，成功或失败
    }

    @Override
    public String orderQuery(String orderGid) {
        logger.info("orderQuery start, orderGid={}", orderGid);
        if (orderGid == null) {
            throw new ChidoriException(ErrorCode.SYS_PARAMETER_ERROR);
        }
        // TODO: 2018/12/10 查询订单信息，核实信息，发起微信支付查询
        TransOrder transOrder = new TransOrder();

        String transStatus = "";

        // 发起微信支付
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(iWxPayConfig);

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", transOrder.getGlobalOrderId());
            data.put("nonce_str", Utility.generateUUID());

            logger.info("发起查询微信支付结果接口, request={}", data);
            Map<String, String> response = wxpay.orderQuery(data);
            logger.info("查询微信支付下单成功, 返回值 response={}", response);
            String returnCode = response.get("return_code");
            if (!SUCCESS.equals(returnCode)) {
                return transStatus;
            }

            String resultCode = response.get("result_code");
            if (!SUCCESS.equals(resultCode)) {
                return transStatus;
            }

            String tradeState = response.get("trade_state");
            switch (tradeState) {
                case GlobalConstants.TradeState.TRADE_STATE_SUCCESS:
                    transStatus = "SUCCESS";
                    break;
                case GlobalConstants.TradeState.TRADE_STATE_CLOSED:
                case GlobalConstants.TradeState.TRADE_STATE_REVOKED:
                case GlobalConstants.TradeState.TRADE_STATE_PAYERROR:
                    transStatus = "FAIL";
                    break;
                case GlobalConstants.TradeState.TRADE_STATE_REFUND:
                    transStatus = "FAIL";
                    break;
                case GlobalConstants.TradeState.TRADE_STATE_NOTPAY:
                    // 订单超过10分钟未支付，取消订单
                    if (transOrder.getCreateTime() < Utility.getCurrentTimeStamp() - 600) {
                        int status = this.cancelOrder(transOrder);
                        if (status == 1) {
                            transStatus = "FAIL";
                        }
                    }
                    break;
                case GlobalConstants.TradeState.TRADE_STATE_USERPAYING:
                    break;
                default:
                    break;
            }

            if ("SUCCESS".equals(transStatus)) {
                String totalFee = response.get("total_fee");
                if (transOrder.getOrderAmount().multiply(new BigDecimal(100)).compareTo(new BigDecimal(totalFee)) != 0) {
                    logger.info("orderQuery totalFee is error, orderGid={}", orderGid);
                    return null;
                }
                this.payCallback(transOrder.getGlobalOrderId(), transStatus);
            } else if ("FAIL".equals(transStatus)) {
                this.payCallback(transOrder.getGlobalOrderId(), transStatus);
            } else {
                return transStatus;
            }
        } catch (Exception e) {
            logger.info("orderQuery Exception is error, e={}", e);
        }

        return transStatus;
    }

    private int cancelOrder(TransOrder transOrder) {
        int resultFlag = 0;
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(iWxPayConfig);

            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", transOrder.getGlobalOrderId());
            data.put("nonce_str", Utility.generateUUID());

            logger.info("发起取消支付订单接口, request={}", data);
            Map<String, String> response = wxpay.closeOrder(data);
            logger.info("取消微信支付订单成功, 返回值 response={}", response);
            String returnCode = response.get("return_code");
            if (!SUCCESS.equals(returnCode)) {
                return resultFlag;
            }

            String resultCode = response.get("result_code");
            if (!SUCCESS.equals(resultCode)) {
                return resultFlag;
            }

            // 取消成功
            this.payCallback(transOrder.getGlobalOrderId(), "FAIL");
            resultFlag = 1;
        } catch (Exception e) {
            logger.info("orderQuery Exception is error, e={}", e);
        }
        return resultFlag;
    }
}
