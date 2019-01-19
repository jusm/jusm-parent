package core.com.wx.service.impl;

import core.com.wx.service.WxTokenService;
import core.com.wx.utils.WxWebUrl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractWxService {

    @Autowired
    private WxTokenService wxTokenService;

    public String fmtReqWebsUrl(WxWebUrl websUrl, Map<String, String> urlParamMap) {
        String url = websUrl.getUrl();
        if (urlParamMap == null) {
            urlParamMap = new HashMap<>();
        }
        // replace URL param
        if (url.contains(WxWebUrl.UrlParam.ACCESS_TOKEN)) {
            // get access token
            String acToken = wxTokenService.getWxToken();
            if (StringUtils.isBlank(acToken)) {
                throw new IllegalStateException("access token invalid.");
            }
            urlParamMap.put(WxWebUrl.UrlParam.ACCESS_TOKEN, acToken);
        }
        for (Map.Entry<String, String> entry : urlParamMap.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (StringUtils.isNotBlank(key) && val != null) {
                url = url.replace(key, val);
            }
        }
        return url;
    }
}
