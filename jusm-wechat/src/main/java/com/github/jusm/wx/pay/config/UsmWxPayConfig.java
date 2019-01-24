package com.github.jusm.wx.pay.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.github.jusm.wx.pay.sdk.IWXPayDomain;
import com.github.jusm.wx.pay.sdk.WXPayConfig;
import com.github.jusm.wx.pay.sdk.WXPayConstants;

public class UsmWxPayConfig extends WXPayConfig {

	private byte[] certData;

	private String app_id;

	/**
	 * API密钥
	 */
	private String wx_pay_key;

	private String wx_pay_mch_id;

	public UsmWxPayConfig(String appid,String wx_pay_key,String mch_id) throws Exception {
		
		this.app_id = appid;
		this.wx_pay_key = wx_pay_key;
		this.wx_pay_mch_id = mch_id;
//		String certPath = "/data/config/apiclient_cert.p12";
//		File file = new File(certPath);
//		InputStream certStream = new FileInputStream(file);
//		this.certData = new byte[(int) file.length()];
//		certStream.read(this.certData);
//		certStream.close();
	}

	@Override
	public String getAppID() {
		return app_id;
	}

	@Override
	public String getMchID() {
		return wx_pay_mch_id;
	}

	@Override
	public String getKey() {
		return wx_pay_key;
	}

	@Override
	public InputStream getCertStream() {
		return new ByteArrayInputStream(this.certData);
	}

	@Override
	public IWXPayDomain getWXPayDomain() {
		IWXPayDomain iwxPayDomain = new IWXPayDomain() {
			@Override
			public void report(String domain, long elapsedTimeMillis, Exception ex) {

			}

			@Override
			public DomainInfo getDomain(WXPayConfig config) {
				return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
			}
		};
		return iwxPayDomain;
	}
}
