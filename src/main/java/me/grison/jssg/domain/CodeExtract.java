package me.grison.jssg.domain;

import java.util.List;

/**
 * This represents an extract of code snippets from some markup.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class CodeExtract {
    String markup;
    List<Code> snippets;

    public CodeExtract(String markup, List<Code> snippets) {
        this.markup = markup;
        this.snippets = snippets;
    }

    public String getMarkup() {
        return this.markup;
    }

    public List<Code> getSnippets() {
        return this.snippets;
    }
}
