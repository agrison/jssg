package me.grison.jssg.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple context class.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class Context {
    /**
     * Environment
     */
    Map<String, Object> env = new HashMap<String, Object>();

    public void put(String key, Object value) {
        this.env.put(key, value);
    }

    public Object get(String key) {
        return this.env.get(key);
    }

    public Map<String, Object> get() {
        return this.env;
    }
}
