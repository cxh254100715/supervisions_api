package com.supervisions.supervisions_api;

import com.supervisions.common.utils.MD5Util;
import com.supervisions.common.utils.RSAUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by humf.需要依赖 commons-codec 包
 */
public class Test
{
    public static void main (String[] args) throws Exception {
        /*Map<String, String> keyMap = RSAUtils.createKeys(1024);
        //String  publicKey = keyMap.get("publicKey");
        //String  privateKey = keyMap.get("privateKey");
        //String publicKey = keyMap.get("publicKey");
        //String  privateKey = keyMap.get("privateKey");
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCbFW_6b0lZaXcrEpRcZ8x42SCZ-mRqTnCq-NiHVCDElNZ56el5ElnupPwlv68C7x-POTo1ErnlRF1lpz4iVfnR4rI0-_JPyUE0wyHd3qVqZFLHI4OMbu0Q20o4By1h2TNjruDumJMg5-gH6B1XAuSulY2P5tMFZUae4JuycCD7QIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIJsVb_pvSVlpdysSlFxnzHjZIJn6ZGpOcKr42IdUIMSU1nnp6XkSWe6k_CW_rwLvH485OjUSueVEXWWnPiJV-dHisjT78k_JQTTDId3epWpkUscjg4xu7RDbSjgHLWHZM2Ou4O6YkyDn6AfoHVcC5K6VjY_m0wVlRp7gm7JwIPtAgMBAAECgYBtycRlSN1wHaEBogbtiDf6RvDU1aRIeaultAw1C3g63bJMfDUj8IfaSbU_Udwdb8O8gIYxeIOVbmbEFYUNso_LatGeM4VXhX-m9zRLbnZp0oLltbqAy4vBTT_6H2iO6fOIoKAXU5jKFy9FqcN0qhKf1aFBnJJO-yb1289fiOUAAQJBAM9tLhmQKIKM2sPdZZLgoR43I40YKBk3zlxxzQjvqtQwPCnESoUNXvBDLb2uLlExUKHjZBplCDmKSk6DiRq2pwECQQCg9vRkrWLp7OUzlixsUHzRh6EPm6jjmAiIiPxc9GZh0ggYJ6tOpinwDQSt7pKP3hKwM8CiJlKlcaOdhzv-T-jtAkAd5zeGJ-ovJ-9B3WEPU6LmoDxX0uu09v6hBSbx6ttNy8ZgnxO_KrZNHOIzPjEfM2TUcdu0kmwlH3Km7v-NAlgBAkB0p4UnEaGF9vGFGWjIdux-zdSkAyO7DgvhJ6X9mAI7EoRbPSShszrhhg3GIAiHy2Gv4VHSMgPjcyAookGQZUlhAkA6TEVh432s0sE1g0nlSnCRI-iG0JNjXja4cEYEd8Vi2yKlh8jufm9oUblPUDA_sT5AjuG4-q8Tgz0ggMNSqbYI";
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);

        String str = "A0SAL7BcEeTe6DXpWUOzcbOooCOr/X7B77JAAy4gRrmXvJQG4r2FwZtzg9r7MKFSI/DVtFi95RNPRLSciRLkHg==";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(publicKey));
        System.out.println("公钥加密密文：\r\n" + encodedData);
        String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(privateKey));
        System.out.println("私钥解密后文字: \r\n" + decodedData);
        String encodedData1 = RSAUtils.privateEncrypt(str, RSAUtils.getPrivateKey(privateKey));
        System.out.println("私钥加密密文：\r\n" + encodedData1);
        String decodedData1 = RSAUtils.publicDecrypt(encodedData1, RSAUtils.getPublicKey(publicKey));
        System.out.println("公钥解密后文字: \r\n" + decodedData1);*/

        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()));
        System.out.println(timestamp);
        String deviceSN = "ABCD1234";
        String deviceUnique = "A0SAL7BcEeTe6DXpWUOzcbOooCOr/X7B77JAAy4gRrmXvJQG4r2FwZtzg9r7MKFSI/DVtFi95RNPRLSciRLkHg==";
        System.out.println(deviceUnique);
        String signString = timestamp + deviceSN + deviceUnique;
        String sign = MD5Util.encode(signString);
        System.out.println(sign);

        String token = "c5764682-97e5-43f7-a1c0-8b72a1a066e8";
        System.out.println(MD5Util.encode("token="+token+"&deviceID="+deviceUnique));

        String paramter = "abcd=b2&bcde=a1";
        String nonce = "zxcq1";
        String timestampStr = "1530956427087";
        String str = paramter+token+token+timestampStr+nonce;
        System.out.println(str);
        System.out.println(MD5Util.encode(str));

    }
}
