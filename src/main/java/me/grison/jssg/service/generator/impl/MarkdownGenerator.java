package me.grison.jssg.service.generator.impl;

import java.util.Arrays;
import java.util.List;

import me.grison.jssg.service.generator.BaseGenerator;
import me.grison.jssg.service.generator.GeneratorService;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * Generator using Markdown.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class MarkdownGenerator extends BaseGenerator implements GeneratorService {
    public boolean accept(String filename) {
        String name = filename.toLowerCase();
        return name.endsWith(".mkd") || name.endsWith(".markdown");
    }

    public List<String> acceptedExtensions() {
        return Arrays.asList("mkd", "markdown", "mk");
    }

    public String markupToHtml(String markup) {
        MarkdownProcessor mkd = new MarkdownProcessor();
        return mkd.markdown(markup);
    }

    public String codify(String text) {
        return "`" + text + "`";
    }
}
