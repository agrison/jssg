package me.grison.jssg.service.app;

import java.io.IOException;

/**
 * Jssg main service interface.  
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface JssgService {
    /** 
     * Generate the static web site.
     */
    public void generate(boolean backup);

    /**
     * Init the directory structure.
     * Creates the following directory:
     * _posts/
     * _layout/
     * And a demo post and index landing page
     * _posts/yyyy-MM-dd-Hello.mkd
     * index.textile
     */
    public void init() throws IOException;
}
