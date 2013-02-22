package me.grison.jssg.service.file.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.grison.jssg.cfg.AppConfig;
import me.grison.jssg.domain.Filename;
import me.grison.jssg.ex.JssgException;
import me.grison.jssg.service.domain.HeaderService;
import me.grison.jssg.service.file.FileService;
import me.grison.jssg.service.file.FilenameService;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.io.Files;

/**
 * File service implementation.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class FileServiceImpl implements FileService {
    @Autowired
    AppConfig appConfig;
    @Autowired
    HeaderService headerService;
    final FilenameService filenameService;
    final String path;
    final String description;
    final String id;

    public FileServiceImpl(FilenameService filenameService, String path, String description, String id) {
        this.filenameService = filenameService;
        this.path = path;
        this.description = description;
        this.id = id;
    }

    public List<File> list() {
        final FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return FileServiceImpl.this.filenameService.isValid(file.getName());
            }
        };
        File[] aFiles = new File(this.path).listFiles(filter);
        List<File> files = new ArrayList<File>();
        if (aFiles != null) {
            files.addAll(Arrays.asList(aFiles));
        }
        return files;
    }

    public String pathForBuiltPost(String filename) {
        Filename fn = this.filenameService.parse(filename);
        return String.format("%s/%s", this.appConfig.buildPath(), fn.toPath());
    }

    public void writeToFile(String content, File file) {
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            Files.write(content.getBytes(this.appConfig.getCharset()), file);
        } catch (IOException e) {
            throw new JssgException("Exception trying to write content into " + file.getAbsolutePath(), e);
        }
    }

    public String readFile(File file) {
        try {
            return Files.toString(file, this.appConfig.getCharset());
        } catch (IOException e) {
            throw new JssgException("Exception trying to read from " + file.getAbsolutePath(), e);
        }
    }

    public String description() {
        return this.description;
    }

    public String path() {
        return this.path;
    }

    public String id() {
        return this.id;
    }
}
