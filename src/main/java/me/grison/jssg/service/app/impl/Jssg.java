package me.grison.jssg.service.app.impl;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.grison.jssg.cfg.AppConfig;
import me.grison.jssg.domain.Context;
import me.grison.jssg.domain.Header;
import me.grison.jssg.ex.JssgException;
import me.grison.jssg.service.app.JssgService;
import me.grison.jssg.service.file.FileChangeService;
import me.grison.jssg.service.file.FileChangedAction;
import me.grison.jssg.service.file.FileService;
import me.grison.jssg.service.file.FilenameService;
import me.grison.jssg.service.generator.GeneratorService;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

/**
 * Jssg service implemenation.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class Jssg implements JssgService {
    private final static Logger LOG = LoggerFactory.getLogger(Jssg.class);
    @Autowired
    List<FileService> fileServices;
    @Autowired
    List<GeneratorService> generatorServices;
    @Autowired
    AppConfig appConfig;
    @Autowired
    FileChangeService fileChangeService;
    private final Context env = new Context();

    /**
     * TODO make use of Optional here.
     * 
     * @param filename
     * @return
     */
    private GeneratorService selectGenerator(String filename) {
        GeneratorService generator = null;
        for (GeneratorService service : this.generatorServices) {
            if (service.accept(filename)) {
                generator = service;
                break;
            }
        }
        return generator;
    }

    private void prepareBuildDirectory(boolean backup) {
        File buildDir = new File(this.appConfig.buildPath());
        if (!backup) {
            if (buildDir.exists()) {
                buildDir.delete();
            }
            return;
        }
        if (!buildDir.exists()) {
            buildDir.mkdir();
            LOG.debug("Creating build directory at: " + buildDir.getAbsolutePath());
            return;
        }
        // the directory already exists, backup it
        File buildBackupDir = new File(this.appConfig.buildBackupPath());
        try {
            // if already exists delete the previous backup
            if (buildBackupDir.exists()) {
                buildBackupDir.delete();
            }
            Files.move(buildDir, buildBackupDir);
            // recreate the build directory
            new File(this.appConfig.buildPath()).mkdir();
        } catch (IOException e) {
            throw new JssgException("Could not backup previous build directory.", e);
        }
    }

    public void init() throws IOException {
        new File(this.appConfig.layoutPath()).mkdirs();
        new File(this.appConfig.postsPath()).mkdirs();
        // copy config.properties
        FileUtils.copyURLToFile(getClass().getResource("/sample/config.properties"), new File("./config.properties"));
        // copy index.textile
        FileUtils.copyURLToFile(getClass().getResource("/sample/index.textile"), new File("./index.textile"));
        // copy layouts
        FileUtils.copyURLToFile(getClass().getResource("/sample/layout.index.html"), new File(this.appConfig.layoutPath() + "/" + "index.html"));
        FileUtils.copyURLToFile(getClass().getResource("/sample/layout.blog.html"), new File(this.appConfig.layoutPath() + "/" + "blog.html"));
        // write the blog post
        String blogPost = CharStreams.toString(new InputStreamReader(this.getClass().getResourceAsStream("/sample/blog.markdown"), Charsets.UTF_8));
        String date1 = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String date2 = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        blogPost = blogPost.replace("DATE1", date1).replace("DATE2", date2);
        FileUtils.writeStringToFile(new File(this.appConfig.postsPath() + "/" + date1 + "-Hello.mkd"), blogPost);
    }

    public void generate(boolean backup) {
        prepareBuildDirectory(backup);
        for (FileService fileService : this.fileServices) {
            System.out.println("\n" + fileService.description() + " in " + fileService.path());
            generateSubset(fileService);
        }
        // copy all files except jssg special directories and handled markup files.
        try {
            copyStaticFiles();
        } catch (Exception e) {
            throw new JssgException("Could not move a static file to build directory.", e);
        }
    }

    void copyStaticFiles() throws Exception {
        FileFilter staticFileFilter = new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                if (name.startsWith("_")) {
                    return false;
                } else {
                    for (String ext : FilenameService.MARKUP_VALID_EXTENSIONS) {
                        if (name.endsWith(ext)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        };
        for (File file : new File(this.appConfig.basePath()).listFiles(staticFileFilter)) {
            File dest = new File(this.appConfig.buildPath() + "/" + file.getName());
            if (file.isDirectory()) {
                FileUtils.copyDirectory(file, dest);
            } else {
                FileUtils.copyFile(file, dest);
            }
        }
    }

    Header buildFile(FileService fileService, File file) {
        Header fileHeader = null;
        String finalPath = fileService.pathForBuiltPost(file.getName());
        GeneratorService generator = selectGenerator(file.getName());
        if (generator != null) {
            // convert markup to html + layout
            String fileContent = fileService.readFile(file);
            fileHeader = generator.extractHeader(fileContent);
            String html = generator.generateHtml(fileContent, this.env);
            fileService.writeToFile(html, new File(finalPath));
        } else {
            System.err.println("Could not find a valid generator!");
        }
        return fileHeader;
    }

    void generateSubset(final FileService fileService) {
        final List<File> files = fileService.list();
        final List<Header> headers = new ArrayList<Header>();
        System.out.printf("  About to process %d files\n", files.size());
        for (File file : files) {
            System.out.println("    + Processing " + file.getName() + "... ");
            Header fileHeader = buildFile(fileService, file);
            headers.add(fileHeader);
            this.fileChangeService.addAction(new FileChangedAction() {
                public void fileHasChanged(File file) {
                    System.out.println("Refreshing " + file.getName() + "... ");
                    buildFile(fileService, file);
                }
            }, file);
        }
        this.env.put(fileService.id(), headers);
    }
}
