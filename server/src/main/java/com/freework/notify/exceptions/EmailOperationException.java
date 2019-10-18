package com.freework.notify.exceptions;

import org.apache.commons.mail.EmailException;

/**
 * @author daihongru
 */
public class EmailOperationException extends EmailException {
    public EmailOperationException(String msg) {
        super(msg);
    }
}
