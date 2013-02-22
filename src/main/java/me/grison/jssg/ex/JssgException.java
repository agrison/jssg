/*
 * Contractor: ARHS-Developments.
 *
 * $HeadURL: http://forge.aris-lux.lan/svn/op-tmon/trunk/arhs-procedure/config/eclipse/resources/ARHS%20-%20Java%20Code%20Templates.xml $
 * $Revision: 9 $
 * $Date: 2011-11-24 17:01:35 +0100 (jeu., 24 nov. 2011) $
 * $Author: buissoni $
 */
package me.grison.jssg.ex;

/**
 * Jssg Exception, made runtime.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class JssgException extends RuntimeException {
    private static final long serialVersionUID = 8019348035020439080L;

    /**
     * The default constructor for JssgException.
     */
    public JssgException() {
    }

    /**
     * The default constructor for JssgException.
     * @param message
     */
    public JssgException(String message) {
        super(message);
    }

    /**
     * The default constructor for JssgException.
     * @param cause
     */
    public JssgException(Throwable cause) {
        super(cause);
    }

    /**
     * The default constructor for JssgException.
     * @param message
     * @param cause
     */
    public JssgException(String message, Throwable cause) {
        super(message, cause);
    }
}
