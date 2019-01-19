package core.com.chidori.controller;

import core.com.chidori.model.common.ChidoriException;
import core.com.chidori.model.common.CommonResponse;
import core.com.chidori.service.WxPayService;
import core.com.chidori.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private WxPayService wxPayService;

    @RequestMapping(value = "/wx/do", method = RequestMethod.POST)
    public CommonResponse doPay() {
        CommonResponse response = null;
        try {
            response = Utility.getRightResponse("success", wxPayService.doPay());
        } catch (ChidoriException ex) {
            logger.error("doPay() is error,", ex);
            return Utility.getFailResp(ex.getErrorCode());
        } catch (Exception e) {
            logger.error("doPay() is error,", e);
            return Utility.getFailResp(null);
        }
        return response;
    }
}
