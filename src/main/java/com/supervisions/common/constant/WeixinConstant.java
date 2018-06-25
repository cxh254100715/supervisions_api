package com.supervisions.common.constant;

/**
 * 通用常量信息
 */
public class WeixinConstant
{

    /**微信支付回调地址*/
    public static final String CallbackURL = "";
    /**微信公众号APPID*/
    public static final String APPID = "wx0ed410defc2481be";
    /**微信公众号秘钥*/
    public static final String APPSECRET = "cbbb9fe58e78287893716247a2f51530";
    /**微信公众号token(暂时不需要)*/
    public static final String TOKEN = "9_uTT1KWIK317pUpMtQDHt_LfDY0T8Jsd66eTLpBCUT84RE7ISONwyL5a5dgAPC6dyAsU_gCALsrnCMTHyK28PtsAkqF8GkOBqMvZXWBQpToCLDW3_BeiK2I3-kwlburRwOuA5O8hG4neAKZyhLOLcAJAWZW";
    /**微信公众号随机64位字符串（如果是重置了，那么就必须重新修改）*/
    public static final String AESKEY = "";
    /**商户秘钥*/
    public static final String MCH_KEY = "";
    /**商户账号*/
    public static final String MCH_ID = "";
    /**微信支付签名方式*/
    public static final String SIGN_TYPE = "MD5";
    /**通过code换取网页授权access_token*/
    public static final String OAUTH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    /**获取微信js_api调用接口权限*/
    public static final String JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    /**获取接口调用凭证access_token用于其它接口的调用*/
    public static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**统一下单URL */
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**生成带参数二维码*/
    public static final String QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";
    /**新增永久素材地址**/
    public static final String  SCURL="https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";

    public static final String MSGMODEL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    public static final String MEDIAURL= "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    //public static final String MODEL1 = "JmEHMLZDdbT_qx5plX9tGHMZ9NhRbkToPXmqUwCSPOI";
    //public static final String MODEL2 = "s0kBkAteNYE9qGTdKVFUkGT3NL_4XOL3z76F7cfuius";

    /**
     * 获取微信用户信息接口地址
     */
    public static final String USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=TOKEN&openid=OPENID&lang=zh_CN";
}