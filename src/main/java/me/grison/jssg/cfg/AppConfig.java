package me.grison.jssg.cfg;

import java.nio.charset.Charset;
import java.util.Locale;

import me.grison.jssg.service.app.JssgService;
import me.grison.jssg.service.app.impl.Jssg;
import me.grison.jssg.service.domain.HeaderService;
import me.grison.jssg.service.domain.impl.PostHeader;
import me.grison.jssg.service.file.FileChangeService;
import me.grison.jssg.service.file.FileService;
import me.grison.jssg.service.file.FilenameService;
import me.grison.jssg.service.file.impl.FileChangeServiceImpl;
import me.grison.jssg.service.file.impl.FileServiceImpl;
import me.grison.jssg.service.file.impl.MarkupFilenameService;
import me.grison.jssg.service.file.impl.PostFilenameService;
import me.grison.jssg.service.fmt.FormatterService;
import me.grison.jssg.service.fmt.TocService;
import me.grison.jssg.service.fmt.impl.JygmentsFormatter;
import me.grison.jssg.service.fmt.impl.TocServiceImpl;
import me.grison.jssg.service.generator.GeneratorService;
import me.grison.jssg.service.generator.impl.MarkdownGenerator;
import me.grison.jssg.service.generator.impl.TextileGenerator;
import me.grison.jssg.service.tpl.TemplateService;
import me.grison.jssg.service.tpl.impl.FreemarkerService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Application Configuration.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
@Configuration
public class AppConfig {
    private String baseDir = "./";
    @Value("${charset:UTF-8}")
    private String charsetProperty;
    @Value("${build_path:/_build/}")
    private String buildPath;
    @Value("${layout_path:/_layout/}")
    private String layoutPath;
    @Value("${posts_path:/_posts/}")
    private String postsPath;
    @Value("${locale:en_US}")
    private String locale;
    @Value("${pygments}")
    private String pygments;

    private Locale getLocale() {
        return Locale.ENGLISH;
    }

    private String path(final String path) {
        return path.replaceAll("//", "/");
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String basePath() {
        return this.baseDir;
    }

    public String postsPath() {
        return path(basePath() + this.postsPath);
    }

    public String buildPath() {
        return path(basePath() + this.buildPath);
    }

    public String buildBackupPath() {
        return path(basePath() + "/_build.backup/");
    }

    public String layoutPath() {
        return path(basePath() + this.layoutPath);
    }

    public Charset getCharset() {
        return Charset.forName(this.charsetProperty);
    }

    public String pygmentsPath() {
        return this.pygments;//"C:\\Python27\\Scripts\\pygmentize.exe";
    }

    //--------- beans
    @Bean(name = "postsFileService")
    public FileService postsFileService() {
        return new FileServiceImpl(postFilenameService(), postsPath(), "Looking for posts", "posts");
    }

    @Bean(name = "pagesFileService")
    public FileService pagesFileService() {
        return new FileServiceImpl(markupFilenameService(), basePath(), "Looking for pages", "pages");
    }

    @Bean
    public JssgService jssgService() {
        return new Jssg();
    }

    @Bean(name = "textileGenerator")
    public GeneratorService textileGenerator() {
        return new TextileGenerator();
    }

    @Bean(name = "markdownGenerator")
    public GeneratorService markdownGenerator() {
        return new MarkdownGenerator();
    }

    @Bean(name = "postFilenameService")
    public FilenameService postFilenameService() {
        return new PostFilenameService();
    }

    @Bean(name = "markupFilenameService")
    public FilenameService markupFilenameService() {
        return new MarkupFilenameService();
    }

    @Bean
    public HeaderService headerService() {
        return new PostHeader();
    }

    @Bean
    public TemplateService templateService() {
        return new FreemarkerService();
    }

    @Bean
    public FormatterService jygmentsFormatter() {
        return new JygmentsFormatter();
    }

    @Bean
    public TocService tocService() {
        return new TocServiceImpl();
    }

    @Bean
    public FileChangeService fileChangedService() {
        return new FileChangeServiceImpl();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new FileSystemResource[] {new FileSystemResource("config.properties")};
        pspc.setLocations(resources);
        pspc.setIgnoreUnresolvablePlaceholders(true);
        pspc.setIgnoreResourceNotFound(true);
        return pspc;
    }
}
