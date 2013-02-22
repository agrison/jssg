package me.grison.jssg.service.domain;

import me.grison.jssg.domain.Header;

/**
 * Header service for extracting header from a post.
 * 
 * The header is delimited by 5 hyphens and has a `key: value` format.
 * <pre>
 * -----
 * foo: bar
 * baz: boz
 * -----
 * </pre>
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface HeaderService {
    public Header extractHeader(String markup);

    public String removeHeader(String markup, Header header);
}
