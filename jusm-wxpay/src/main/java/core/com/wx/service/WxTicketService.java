package core.com.wx.service;

import java.util.Map;

public interface WxTicketService {

    public String getJSAPITicket();

    public Map<String, String> getSdkConfig(String url);

    public String payBack(String notifyData);
}
