package me.grison.jssg.service.file;

import java.io.File;

/**
 * Interface defining a simple method to deal with file changes.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface FileChangedAction {
    /**
     * Invoked when a file changes.
     * 
     * @param fileName the file that has changed.
     */
    void fileHasChanged(File file);
}