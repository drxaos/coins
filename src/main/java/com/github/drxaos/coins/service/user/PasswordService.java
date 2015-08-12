package com.github.drxaos.coins.service.user;

import com.github.drxaos.coins.application.*;
import com.github.drxaos.coins.application.config.ApplicationProps;
import com.github.drxaos.coins.application.events.ApplicationInit;
import com.github.drxaos.coins.application.factory.Autowire;
import com.github.drxaos.coins.application.factory.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class PasswordService implements ApplicationInit {

    @Autowire
    ApplicationProps props;

    public String encode(String password) {
        try {
            byte[] salt = props.getString("user.password.salt", "salt").getBytes();
            byte[] digest = password.getBytes();
            for (int i = 0; i < 25; i++) {
                byte[] source = new byte[digest.length + salt.length];
                System.arraycopy(digest, 0, source, 0, digest.length);
                System.arraycopy(salt, 0, source, digest.length, salt.length);

                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(source);
                digest = messageDigest.digest();
            }
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("error", e);
        }
    }

    @Override
    public void onApplicationInit(Application application) throws ApplicationInitializationException {
        try {
            MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationInitializationException("Cannot load SHA-256 algorithm", e);
        }
    }
}
