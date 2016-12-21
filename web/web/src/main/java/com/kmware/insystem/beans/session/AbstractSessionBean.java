package com.kmware.insystem.beans.session;

import org.apache.log4j.Logger;

public abstract class AbstractSessionBean {

    /**
     * Logger to log something if needed
     */
    protected Logger log = Logger.getLogger(getClass());

    public abstract void init();

    /**
     * A shortcut for {@link Log#info(Object)}
     * @param message to log
     */
    protected void log(String message) {
        log.info(message);
    }
}
