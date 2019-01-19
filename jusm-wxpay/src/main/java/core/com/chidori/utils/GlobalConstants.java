package core.com.chidori.utils;

/**
 * Created by wangjianan on 2018/1/25.
 */
public final class GlobalConstants {

    public static class TradeState {
        public static final String TRADE_STATE_SUCCESS = "SUCCESS"; // 支付成功
        public static final String TRADE_STATE_REFUND = "REFUND"; // 转入退款
        public static final String TRADE_STATE_NOTPAY = "NOTPAY"; // 未支付
        public static final String TRADE_STATE_CLOSED = "CLOSED"; // 已关闭
        public static final String TRADE_STATE_REVOKED = "REVOKED"; // 已撤销（刷卡支付）
        public static final String TRADE_STATE_USERPAYING = "USERPAYING"; // 用户支付中
        public static final String TRADE_STATE_PAYERROR = "PAYERROR"; // 支付失败(其他原因，如银行返回失败)
    }
}