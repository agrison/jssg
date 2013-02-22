package me.grison.jssg.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A post header.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class Header {
    public final static String LAYOUT = "layout";
    public final static String TITLE = "title";
    public final static String TOC = "toc";
    public final Map<String, Object> values = new HashMap<String, Object>();
    public final String originalStr;

    public Header(String originalStr) {
        this.originalStr = originalStr;
    }

    public void setValues(Map<String, Object> map) {
        this.values.putAll(map);
    }

    public void setValue(String key, String value) {
        this.values.put(key, value);
        this.values.put("page." + key, value);
    }

    public String getString(String key) {
        return (String) this.values.get(key);
    }

    public String getLayout() {
        return getString(LAYOUT);
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public String getUrl() {
        return getString("url");
    }

    public Date getDate() {
        return (Date) this.values.get("date");
    }

    public boolean hasToc() {
        return this.values.containsKey("toc") && (Boolean) this.values.get(TOC);
    }
}
