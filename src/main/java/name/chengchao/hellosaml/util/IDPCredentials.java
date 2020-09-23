package name.chengchao.hellosaml.util;

import org.apache.commons.io.IOUtils;
import org.opensaml.security.credential.Credential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * 本DEMO 作为IDP 接入时所使用的用来保存 凭据 的类
 */
public class IDPCredentials {
    private static Logger logger = LoggerFactory.getLogger(IDPCredentials.class);
    private static Credential credential;

    private static Credential resolveCredential(String password) {
        try {
            String privateKey = IOUtils.toString(IDPCredentials.class.getResource("/server.key"));
            String certificate = IOUtils.toString(IDPCredentials.class.getResource("/test.pem"));
            return SamlKeyStoreProvider.getCredential(privateKey, certificate, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Credential getCredential(String password) {
        if (credential == null) {
            credential = resolveCredential(password);
            logger.info(EncodingUtils.encode(credential.getPrivateKey().getEncoded())); // 私钥
            logger.info(EncodingUtils.encode(credential.getPublicKey().getEncoded())); // 公钥
        }
        return credential;
    }

    public static Credential readCredential(InputStream inputStream) throws IOException {
        String certificate = IOUtils.toString(inputStream);
        return SamlKeyStoreProvider.getCredential(null, certificate, null);
    }

}
