package core.com.wx.service.impl;

import core.com.chidori.utils.Utility;
import core.com.wx.model.AccessTokenResponse;
import core.com.wx.service.WxTokenService;
import core.com.wx.service.http.HttpService;
import core.com.wx.utils.WxWebUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class WxTokenServiceImpl extends AbstractWxService implements WxTokenService {
    private static final Logger logger = LoggerFactory.getLogger(WxTokenServiceImpl.class);

    @Value("${vendor.wx.config.app_id}")
    private String app_id;

    @Value("${vendor.wx.config.app_secret}")
    private String app_secret;

    private static ConcurrentHashMap<String, String> ACCESS_TOKEN_MAP = new ConcurrentHashMap<>();
    private static final String ACCESS_TOKEN = "access_token";
    private static final String EXPIRES_IN = "expires_in";

    @Autowired
    private HttpService httpService;

    @Override
    public String getWxToken() {
        logger.info("getWxToken() start");
        String accessToken = null;

        if (ACCESS_TOKEN_MAP.get(ACCESS_TOKEN) != null && Integer.parseInt(ACCESS_TOKEN_MAP.get(EXPIRES_IN)) > Utility.getCurrentTimeStamp()) {
            accessToken = ACCESS_TOKEN_MAP.get(ACCESS_TOKEN);
            logger.info("getWxToken() end, access-token from redis, expires_in, accessToken={}", accessToken);
            return accessToken;
        }

        // params
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(WxWebUrl.UrlParam.APP_ID, app_id);
        paramMap.put(WxWebUrl.UrlParam.APP_SECRET, app_secret);
        AccessTokenResponse response = null;
        try {
            response = httpService.get(this.fmtReqWebsUrl(WxWebUrl.GET_WX_TOKEN, paramMap), AccessTokenResponse.class);
        } catch (Exception e) {
            logger.error("getWxToken() exception. ", e);
        }

        if (response != null) {
            accessToken = response.getAccess_token();
            Integer expiresIn = response.getExpires_in();
            ACCESS_TOKEN_MAP.put(ACCESS_TOKEN, accessToken);
            ACCESS_TOKEN_MAP.put(EXPIRES_IN, String.valueOf(expiresIn + Utility.getCurrentTimeStamp() - 600));
        }
        logger.info("getWxToken() end, accessToken={}", accessToken);
        return accessToken;
    }
}
