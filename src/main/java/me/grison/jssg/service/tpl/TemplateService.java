package me.grison.jssg.service.tpl;

import me.grison.jssg.domain.Header;
import me.grison.jssg.domain.Context;

/**
 * Template service.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface TemplateService {
    /**
     * Generate the final HTML using the layout defined in the given header,
     * replacing ${content} with the given HTML content.
     * 
     * @param htmlContent the HTML content for replacement as ${content} in the layout.
     * @param header the header containing the layout and variables to be infered in the layout.
     * @param env the runtime environment.
     * @return the final HTML.
     */
    String generateHtml(String htmlContent, Header header, Context env);
}
