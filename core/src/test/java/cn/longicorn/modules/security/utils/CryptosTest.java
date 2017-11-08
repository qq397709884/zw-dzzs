package cn.longicorn.modules.security.utils;

import cn.longicorn.modules.utils.Encodes;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CryptosTest {
    @Test
    public void mac() {
        String input = "foo message";

        // key可为任意字符串
        // byte[] key = "a foo key".getBytes();
        byte[] key = Cryptos.generateHmacSha1Key();
        Assert.assertEquals(20, key.length);

        byte[] macResult = Cryptos.hmacSha1(input.getBytes(), key);
        System.out.println("hmac-sha1 key in hex      :" + Encodes.encodeHex(key));
        System.out.println("hmac-sha1 in hex result   :" + Encodes.encodeHex(macResult));

        assertTrue(Cryptos.isMacValid(macResult, input.getBytes(), key));
    }

    private String signWithSHA256(Map<String, String> sysParam, String busiParam, String key) {
        Map<String, String> map = new HashMap<>(sysParam);
        map.put("content", busiParam);

        String[] keys = map.keySet().toArray(new String[map.size()]);
        Arrays.sort(keys);

        StringBuilder buf = new StringBuilder(200);
        buf.append(key);
        for (String k : keys) {
            if (!("sign".equalsIgnoreCase(k))) {
                buf.append(k).append(map.get(k));
            }
        }
        buf.append(key);

        byte[] macResult = Cryptos.hmacSha256(buf.toString().getBytes(), Encodes.decodeHex(key));
        return Encodes.encodeHex(macResult);
    }

    @Test
    public void hmac256Sha1() {
        // 系统参数
        Map<String, String> sysParam = new HashMap<String, String>();
        sysParam.put("method", "CUST_QRY_CUST_INFO"); // 能力编码
        sysParam.put("appId", "501220"); // 应用编码
        sysParam.put("format", "json"); // 业务参数和响应报文格式
        sysParam.put("version", "1.0"); // 版本号，默认1.0
        sysParam.put("accessToken", "5ea6f06f-796b-48a2-a825-35a1909adffa"); // 访问令牌，调用令牌接口获取
        sysParam.put("timestamp", "20140928165601"); // 当前时间戳
        sysParam.put("busiSerial", "1"); // 视具体的应用而定

        // 业务参数
        String busiParam = "{\"REGION_ID\":\"A\",\"CERT_TYPE\":\"2\",\"CERT_NO\":\"1234567\"}";
        // 加密密钥，默认是应用密钥(可配置)
        String dataSecret = "501e3f2e8bd3c8b0bad3e16b795dd85b";
        // 业务参数加密

        byte[] encryptResult = Cryptos.aesEncrypt(busiParam.getBytes(), Encodes.decodeHex(dataSecret));
        busiParam = Encodes.encodeHex(encryptResult).toUpperCase();
        System.out.println(busiParam);

        // 应用密钥
        String appKey = "501e3f2e8bd3c8b0bad3e16b795dd85b";
        System.out.println(signWithSHA256(sysParam, busiParam, appKey));
    }

    @Test
    public void aes() {
        byte[] key = Cryptos.generateAesKey();
        assertEquals(16, key.length);
        String input = "foo message";

        byte[] encryptResult = Cryptos.aesEncrypt(input.getBytes(), key);
        String descryptResult = Cryptos.aesDecrypt(encryptResult, key);

        System.out.println("aes key in hex            :" + Encodes.encodeHex(key));
        System.out.println("aes encrypt in hex result :" + Encodes.encodeHex(encryptResult));
        assertEquals(input, descryptResult);
    }

    @Test
    public void aes2() {
        byte[] key = Cryptos.generateAesKey();
        assertEquals(16, key.length);
        String input = "{\"REGION_ID\":\"A\",\"CERT_TYPE\":\"2\",\"CERT_NO\":\"1234567\"}";
        String dataSecret = "501e3f2e8bd3c8b0bad3e16b795dd85b";

        byte[] encryptResult = Cryptos.aesEncrypt(input.getBytes(), Encodes.decodeHex(dataSecret));

        System.out.println("aes key in hex            :" + Encodes.encodeHex(key));
        System.out.println("aes encrypt in hex result :" + Encodes.encodeHex(encryptResult));
    }

    @Test
    public void aesWithIV() {
        byte[] key = Cryptos.generateAesKey();
        byte[] iv = Cryptos.generateIV();
        assertEquals(16, key.length);
        assertEquals(16, iv.length);
        String input = "foo message";

        byte[] encryptResult = Cryptos.aesEncrypt(input.getBytes(), key, iv);
        String descryptResult = Cryptos.aesDecrypt(encryptResult, key, iv);

        System.out.println("aes key in hex            :" + Encodes.encodeHex(key));
        System.out.println("iv in hex                 :" + Encodes.encodeHex(iv));
        System.out.println("aes encrypt in hex result :" + Encodes.encodeHex(encryptResult));
        assertEquals(input, descryptResult);
    }
}
