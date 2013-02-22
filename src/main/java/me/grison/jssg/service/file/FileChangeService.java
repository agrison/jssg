package me.grison.jssg.service.file;

import java.io.File;

public interface FileChangeService {
    /**
     * What to do if the given file has changed.
     * 
     * @param listener the action to be taken.
     * @param file the file watched for changes.
     */
    void addAction(FileChangedAction action, File file);
}
