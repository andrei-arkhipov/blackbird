package com.kmware.insystem.beans.helper;

import org.jboss.security.auth.spi.DatabaseServerLoginModule;

import javax.security.auth.login.LoginException;

/**
 *
 * Author: Kirill Bazarov
 */

public class LoginModule extends DatabaseServerLoginModule {
    private String[] info;
    @Override
    protected String[] getUsernameAndPassword(){
        try {
            info = super.getUsernameAndPassword();
            info[1] = Crypt.encrypt(info[1]);
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return info;
    }
}
