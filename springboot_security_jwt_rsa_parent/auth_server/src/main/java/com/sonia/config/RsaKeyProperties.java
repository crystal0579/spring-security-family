package com.sonia.config;

import com.sonia.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

//@Configuration//表示文件是个 xml的配置文件, 不以bean的方式注入到spring容器中
@ConfigurationProperties("rsa.key")
public class RsaKeyProperties {
    /**
     * 这是来源于application.yml 的两个字符串
     */
    private String publicKeyFile;

    private String privateKeyFile;

    /**
     * 这是用来初始化 spring security 的对象
     */
    private PublicKey publicKey;

    private PrivateKey privateKey;

    @PostConstruct //表示在两个 String 属性的 publicKeyFile 和 privateKeyFile 注入完成之后执行的方法
    public void createRsaKey() throws Exception {
        publicKey = RsaUtils.getPublicKey(getPublicKeyFile());
        privateKey = RsaUtils.getPrivateKey(getPrivateKeyFile());
    }

    public String getPublicKeyFile() {
        return publicKeyFile;
    }

    public void setPublicKeyFile(String publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
