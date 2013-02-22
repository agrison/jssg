package me.grison.jssg.service.domain.impl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.grison.jssg.domain.Header;
import me.grison.jssg.service.domain.HeaderService;

import org.yaml.snakeyaml.Yaml;

/**
 * Post header service implementation.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class PostHeader implements HeaderService {
    final Matcher headerMatcher = Pattern.compile("^([-]{3,}(.*?)[-]{3,}).*$", Pattern.DOTALL).matcher("");

    @SuppressWarnings("unchecked")
    public Header extractHeader(String markup) {
        if (this.headerMatcher.reset(markup).matches()) {
            Header header = new Header(this.headerMatcher.group(1));
            Map<String, Object> headerValues = (Map<String, Object>) new Yaml().load(this.headerMatcher.group(2));
            header.setValues(headerValues);
            return header;
        } else {
            return null;
        }
    }

    public String removeHeader(String markup, Header header) {
        return header == null ? markup : markup.substring(header.originalStr.length());
    }
}
