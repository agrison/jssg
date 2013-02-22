package me.grison.jssg.service.file.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.grison.jssg.domain.Filename;
import me.grison.jssg.service.file.FilenameService;

/**
 * Filename service implementation for any markup file that is not a post.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class MarkupFilenameService extends BaseFilenameService implements FilenameService {
    final static Matcher FILE_MATCHER = Pattern.compile("^(.*?)[.]([^.]+)$").matcher("");

    public boolean isValid(final String filename) {
        loadExtensionsIfNeeded();
        return checkExtension(filename);
    }

    public Filename parse(String filename) {
        if (!FILE_MATCHER.reset(filename).matches()) {
            return null;
        } else {
            return new Filename("", FILE_MATCHER.group(1), FILE_MATCHER.group(2));
        }
    }
}
