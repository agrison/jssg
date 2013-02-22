package me.grison.jssg.service.file.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.grison.jssg.ex.JssgException;
import me.grison.jssg.service.file.FileChangeService;
import me.grison.jssg.service.file.FileChangedAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class FileChangeServiceImpl implements FileChangeService {
    private final static Logger LOG = LoggerFactory.getLogger(FileChangeServiceImpl.class);
    private final Timer timer = new Timer(true); // daemon
    private final Map<String, Task> timerEntries = new HashMap<String, Task>();
    private final long timerPeriod = 5000L;

    public void addAction(FileChangedAction listener, File file) {
        Task task = new Task(listener, file);
        this.timerEntries.put(file.toString() + listener.hashCode(), task);
        this.timer.schedule(task, this.timerPeriod, this.timerPeriod);
    }
    /**
     * File monitoring task.
     */
    class Task extends TimerTask {
        private final FileChangedAction listener;
        private final File monitoredFile;
        private long lastModified;

        public Task(FileChangedAction listener, File file) {
            this.listener = listener;
            this.lastModified = 0;
            this.monitoredFile = file;
            if (!this.monitoredFile.exists()) {
                throw new JssgException("Could not monitor file `" + file.getAbsolutePath() + "` for changes, as it does not exists.");
            }
            this.lastModified = this.monitoredFile.lastModified();
        }

        @Override
        public void run() {
            long lastModified = this.monitoredFile.lastModified();
            if (lastModified != this.lastModified) {
                this.lastModified = lastModified;
                LOG.debug("File `" + this.monitoredFile.getAbsolutePath() + "` has changed.");
                this.listener.fileHasChanged(this.monitoredFile);
            }
        }
    }
}