package me.grison.jssg.service.file;

import java.util.ArrayList;
import java.util.List;

import me.grison.jssg.domain.Filename;

/** 
 * List the files of the website.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface FilenameService {
    final static List<String> MARKUP_VALID_EXTENSIONS = new ArrayList<String>();

    /**
     * Returns whether the given filename is valid.
     * That is, if a loaded Generator can handle it,
     * and if it verifies the FilenameService.DATED_FILE_MATCHER.
     * 
     * @param filename the filename.
     * @return whether the given filename is valid.
     */
    public boolean isValid(String filename);

    public Filename parse(String filename);
}
