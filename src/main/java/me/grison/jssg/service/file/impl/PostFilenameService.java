package me.grison.jssg.service.file.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.grison.jssg.domain.Filename;
import me.grison.jssg.service.file.FilenameService;

/**
 * Filename service implementation for posts.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class PostFilenameService extends BaseFilenameService implements FilenameService {
    final static Matcher DATED_FILE_MATCHER = Pattern.compile("^([0-9]{4}-[0-9]{2}-[0-9]{2}).(.*?)[.]([^.]+)$").matcher("");

    public boolean isValid(final String filename) {
        loadExtensionsIfNeeded();
        if (!DATED_FILE_MATCHER.reset(filename).matches()) {
            return false;
        }
        return checkExtension(filename);
    }

    public Filename parse(String filename) {
        if (!DATED_FILE_MATCHER.reset(filename).matches()) {
            return null;
        } else {
            return new Filename(DATED_FILE_MATCHER.group(1), DATED_FILE_MATCHER.group(2), DATED_FILE_MATCHER.group(3));
        }
    }
}
