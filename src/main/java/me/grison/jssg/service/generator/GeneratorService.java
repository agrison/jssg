package me.grison.jssg.service.generator;

import java.util.List;

import me.grison.jssg.domain.Header;
import me.grison.jssg.domain.Context;

/**
 * Interface for the Generator Service.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface GeneratorService {
    /**
     * Generate the HTML from the given markup, including layout defined in the markup header.
     * 
     * @param markup the markup.
     * @param env the runtime environment. 
     * @return the HTML ready to be written to the file system.
     */
    String generateHtml(String markup, Context env);

    /** 
     * Extract the header from the given markup.
     * 
     * @param markup the markup.
     * @return the header extracted from the markup.
     */
    public Header extractHeader(String markup);

    /**
     * Returns whether this GeneratorService instance accepts the given filename.
     * 
     * @param filename the filename.
     * @return whether the file can be accepted.
     */
    public boolean accept(String filename);

    /**
     * Accepted extensions.
     * 
     * @return the accepted extensions.
     */
    public List<String> acceptedExtensions();

    /**
     * Return the text as Code in the underlying markup language.
     * 
     * @param text the text to be represented as text.
     * @return the text as code.
     */
    public String codify(String text);

    /**
     * Generate HTML from the given markup.
     * @param markup the markup.
     * @return the HTML.
     */
    public String markupToHtml(String markup);
}
