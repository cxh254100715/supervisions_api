package com.supervisions.supervisions_api;

import com.supervisions.common.utils.RSAUtils;

import java.util.Map;

/**
 * Created by humf.需要依赖 commons-codec 包
 */
public class Test
{
    public static void main (String[] args) throws Exception {
        //Map<String, String> keyMap = RSAUtils.createKeys(1024);
        //String  publicKey = keyMap.get("publicKey");
        //String  privateKey = keyMap.get("privateKey");
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNp1palxmguTBywKwppt0F9_uCEjokW3caSn8OrQT1mU2usGncVnhws8D8PHaSZ4spBQ7OpdUlBQk_V6RMNiikcG01hmsoB12YwSjB4voYUlQrSMbRZG7IHDN1owMTfTaWgTHut9A32qZQRdptYXW7Om8fT3fbFaA5x7NkWTSz_wIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAI2nWlqXGaC5MHLArCmm3QX3-4ISOiRbdxpKfw6tBPWZTa6wadxWeHCzwPw8dpJniykFDs6l1SUFCT9XpEw2KKRwbTWGaygHXZjBKMHi-hhSVCtIxtFkbsgcM3WjAxN9NpaBMe630DfaplBF2m1hdbs6bx9Pd9sVoDnHs2RZNLP_AgMBAAECgYAwTb0V3yiyEfRfbSxIF8qO2n09YZHKDBaVNIoXKxDh3yEKTMYMiMARTnqkiMm2KvDdUAg4l3wTX6BDpVM1WdKJnedjXjaaQiz0n5o_oqydezssARrlOv1gD2YCwbup8dVRM6eUmC3ZVZ9mTt4FM7oVaL9MpyXxPhW9LQdroIOwoQJBANNoBV9A7N8osMJ6DDfPc64djZIR52ylVrjtvN-wKxpOlmplbJN9Jfl4WZSlo0U4A13x64HDKFGcY78nTjfH0nsCQQCriK3GXoHNuEmtl8MhGydqVZl-ufkopc4qblcHzEpVcYTpeShhya6jW-wsXk1d0ZGF1YdOel033HwORJlFIJ9NAkBK4sscXdRKjJBoo3EWh_7zS3atqnfu7XaV1WkVV8M881-m2rymViuQfkhFNpNqXpgavCpvceEjX6GZweOcMMVnAkEAnqFciymVAuldgaLqejBBaC3XMIHN8JA9M7Is2_JJ__BmSP-Zn61CyqHUrEddZtjKqikI40N395P4Sb2YvHvptQJATFEVX9fNG1fxN05Af5F7f1nOR4ILmbGvd-xtTWDwPIIsM-hY8iI4YRYVTTig-Ipeb-X9ukTbp9ANERNXCL09YQ";
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);

        String str = "{\"deviceCode\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBQE00In-i5Vq3yk8i5oiZP_Oxc7jYr2mdzJw5nMxFcW0J9PAI-KJtGIDjpllVlfknhtej5ku8axgmVstl2UItBUSsKfUt1CbeQ12Dfn1DYLu2uu\",\"type\":\"3\"}";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(publicKey));
        System.out.println("公钥加密密文：\r\n" + encodedData);
        String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(privateKey));
        System.out.println("私钥解密后文字: \r\n" + decodedData);
        String encodedData1 = RSAUtils.privateEncrypt(str, RSAUtils.getPrivateKey(privateKey));
        System.out.println("私钥加密密文：\r\n" + encodedData1);
        String decodedData1 = RSAUtils.publicDecrypt(encodedData1, RSAUtils.getPublicKey(publicKey));
        System.out.println("公钥解密后文字: \r\n" + decodedData1);
    }
}
