package org.semtag.core.model;

/**
 * @author Ari Weiland
 */
public class SemTagException extends Exception {
    public SemTagException() {
        super();
    }

    public SemTagException(String s) {
        super(s);
    }

    public SemTagException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public SemTagException(Throwable throwable) {
        super(throwable);
    }
}
