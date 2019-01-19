package core.com.chidori.controller;

import core.com.chidori.model.common.ChidoriException;
import core.com.chidori.model.common.CommonResponse;
import core.com.chidori.utils.Utility;
import core.com.wx.service.WxTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
public class WxPayController {
    private final static Logger logger = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    private WxTicketService wxTicketService;

    @RequestMapping(value = "/getSdkConfig", method = RequestMethod.GET)
    public CommonResponse getSdkConfig(@RequestParam String url) {
        logger.info("getSdkConfig() start, url={}", url);
        CommonResponse response = null;
        try {
            response = Utility.getRightResponse("success", wxTicketService.getSdkConfig(url));
        } catch (ChidoriException ex) {
            logger.error("getSdkConfig() is error,", ex);
            return Utility.getFailResp(ex.getErrorCode());
        } catch (Exception e) {
            logger.error("getSdkConfig() is error,", e);
            return Utility.getFailResp(null);
        }
        return response;
    }

    @RequestMapping(value = "/payCallback", method = RequestMethod.POST)
    public String payCallback(HttpServletRequest request, HttpServletResponse response) {
        logger.info("进入微信支付异步通知");
        String resXml="";
        try{
            //
            InputStream is = request.getInputStream();
            //将InputStream转换成String
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml=sb.toString();
            logger.info("微信支付异步通知请求包: {}", resXml);
            return wxTicketService.payBack(resXml);
        }catch (Exception e){
            logger.error("微信支付回调通知失败",e);
            String result = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
            return result;
        }
    }

}
