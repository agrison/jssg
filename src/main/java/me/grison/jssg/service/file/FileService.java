package me.grison.jssg.service.file;

import java.io.File;
import java.util.List;

/** 
 * List the files of the website.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface FileService {
    /**
     * Return the if of this file service.
     * 
     * @return the id.
     */
    public String id();

    /**
     * Return the description of what this file service does.
     * 
     * @return the description.
     */
    public String description();

    /**
     * Return the list of files in the configured file service directory path.
     * Only valid file name are being accepted.
     * 
     * @return the list of file to be included in the resulting website.
     */
    public List<File> list();

    /**
     * Return the path configured for this file service.
     * 
     * @return the path were this file service is looking for files.
     */
    public String path();

    /**
     * Return the final path for the built post depending on the filename.
     * <pre>2012-01-17.filename.textile -> /2012/01/17/filename/index.html</pre>
     * 
     * @param filename the filename.
     * @return the final path were built.
     */
    public String pathForBuiltPost(String filename);

    /**
     * Write the given content into the given file.
     * 
     * @param content the file content.
     * @param file the file where to write.
     */
    public void writeToFile(String content, File file);

    /**
     * Read fully the given file.
     * 
     * @param file the file to read.
     * @return the full file content.
     */
    public String readFile(File file);
}
