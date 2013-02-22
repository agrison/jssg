package me.grison.jssg.domain;

/**
 * This represents some code extracted from markup.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class Code {
    String id;
    String wholeCode;
    String code;
    String lang;
    String htmlCode;

    public Code(String id, String wholeCode, String code, String lang, String htmlCode) {
        this.id = id;
        this.wholeCode = wholeCode;
        this.code = code;
        this.lang = lang;
        this.htmlCode = htmlCode;
    }

    public String getHtmlCode() {
        return this.htmlCode;
    }

    public String getId() {
        return this.id;
    }
}
