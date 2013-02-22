package me.grison.jssg.service.file.impl;

import java.util.List;

import me.grison.jssg.service.file.FilenameService;
import me.grison.jssg.service.generator.GeneratorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Base filename service.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public abstract class BaseFilenameService implements FilenameService {
    private final static Logger LOG = LoggerFactory.getLogger(BaseFilenameService.class);
    @Autowired
    List<GeneratorService> generators;

    void loadExtensionsIfNeeded() {
        if (!FilenameService.MARKUP_VALID_EXTENSIONS.isEmpty()) {
            return;
        }
        for (GeneratorService generator : this.generators) {
            FilenameService.MARKUP_VALID_EXTENSIONS.addAll(generator.acceptedExtensions());
        }
        LOG.debug("Valid extensions are: " + FilenameService.MARKUP_VALID_EXTENSIONS + " both lower and uppercase");
    }

    boolean checkExtension(final String filename) {
        return Iterables.any(FilenameService.MARKUP_VALID_EXTENSIONS, new Predicate<String>() {
            public boolean apply(String extension) {
                return filename.toLowerCase().endsWith(extension);
            }
        });
    }
}
