package me.grison.jssg.service.fmt;

/**
 * Table Of Contents Service.
 *
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface TocService {
    /**
     * Create the toc.
     * 
     * @return the toc.
     */
    public String createToc(String html);

    /**
     * Add a toc to the given html.
     * Modifies the <hX> tags to add an id attribute with a sanitized string representing its content.
     * 
     * @param html the html.
     * @return the html with ids for hX tags
     */
    public String addTocToHtml(String html);
}
