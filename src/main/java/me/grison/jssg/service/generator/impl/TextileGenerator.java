package me.grison.jssg.service.generator.impl;

import java.util.Arrays;
import java.util.List;

import me.grison.jssg.service.generator.BaseGenerator;
import me.grison.jssg.service.generator.GeneratorService;
import net.sf.textile4j.Textile;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Generator using textile.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class TextileGenerator extends BaseGenerator implements GeneratorService {
    public boolean accept(final String filename) {
        return Iterables.any(acceptedExtensions(), new Predicate<String>() {
            public boolean apply(String extension) {
                return filename.toLowerCase().endsWith(extension);
            }
        });
    }

    public List<String> acceptedExtensions() {
        return Arrays.asList("textile", "texti");
    }

    public String markupToHtml(String markup) {
        Textile textile = new Textile();
        return textile.process(markup);
    }

    public String codify(String text) {
        return "@" + text + "@";
    }
}
