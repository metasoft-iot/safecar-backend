package com.safecar.platform.iam.domain.exceptions;

import javax.naming.AuthenticationException;

/**
 * Email Not Found Exception
 * <p>
 * This exception is thrown when an email address is not found during the
 * process.
 * </p>
 */
public class EmailNotFoundException extends AuthenticationException {
    /**
     * Constructor for EmailNotFoundException.
     * 
     * @param message the detail message.
     */
    public EmailNotFoundException(String message) {
        super(message);
    }
}
