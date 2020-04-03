package com.sonia.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class RsaUtilsTest {

    //这两个 恒定变量值 可以写入auth_server Module中的 application.yml
    private static final String privateKeyFilePath = "C:\\Users\\wengj\\Documents\\study\\jwt\\RAS_KEY\\sonia_key_rsa";

    private static final String publicKeyFilePath = "C:\\Users\\wengj\\Documents\\study\\jwt\\RAS_KEY\\sonia_key_rsa.pub";

    @Test
    public void getPublicKey() throws Exception {
        System.out.println(RsaUtils.getPublicKey(publicKeyFilePath));
    }

    @Test
    public void getPrivateKey() throws Exception {
        System.out.println(RsaUtils.getPublicKey(publicKeyFilePath));
    }

    @Test
    public void generateKey() throws Exception {
        RsaUtils.generateKey(publicKeyFilePath, privateKeyFilePath, "sonia is beautiful", 2048);//"sonia is beautiful" 是盐
    }
}