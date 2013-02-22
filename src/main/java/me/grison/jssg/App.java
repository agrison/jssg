package me.grison.jssg;

import static org.kohsuke.args4j.ExampleMode.ALL;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;

import me.grison.jssg.cfg.AppConfig;
import me.grison.jssg.service.app.JssgService;
import me.grison.jssg.service.file.impl.PostFilenameService;
import me.grison.jssg.util.DirectoryTree;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

/**
 * <pre>
 *         .--.
 *       .--,`|   .--.--.      .--.--.      ,----._,.
 *      |  |.   /  /    '    /  /    '    /   /  ' /
 *      '--`_  |  :  /`./   |  :  /`./   |   :     |
 *      ,--,'| |  :  ;_     |  :  ;_     |   | .\  .
 *     |  | '  \  \    `.   \  \    `.  .   ; ';  |
 *     :  | |   `----.   \   `----.   \ '   .   . |
 *   __|  : '  /  /`--'  /  /  /`--'  /  `---`-'| |
 * .'__/\_: | '--'.     /  '--'.     /   .'__/\_: |
 * |   :    :   `--'---'     `--'---'    |   :    :
 * \   \  /                              \   \  /
 *  `--`-'                                `--`-'
 * </pre>
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class App {
    static {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    }
    private final static Logger LOG = LoggerFactory.getLogger(PostFilenameService.class);
    private AppConfig appConfig;
    private JssgService jssgApp;
    // Command line stuf
    @Option(name = "-init", usage = "Init a directory structure", aliases = "init")
    private boolean init;
    @Option(name = "-build", usage = "Build the static website", aliases = {"build", "generate"})
    private boolean build;
    @Option(name = "-serve", usage = "Serve the website on localhost", aliases = {"serve", "server"})
    private boolean serve;
    @Option(name = "-port", usage = "Specify a special port for the localhost server", aliases = "port")
    private final int port = 9876;
    @Option(name = "-tree", usage = "Show the build content tree", aliases = "tree")
    private boolean tree;
    @Option(name = "-backup", usage = "Backup tue build directory", aliases = "backup")
    private boolean backup;

    /**
     * Shows the given file content on sysout.
     *
     * @param file the file.
     */
    void showResourceFile(final String file) {
        try {
            InputStream is = this.getClass().getResourceAsStream(file);
            System.out.println(CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8)));
        } catch (IOException e) {
            LOG.error("Error loading the file content `" + file + "`.", e);
        }
    }

    /**
     * Run the application.
     * 
     * @param args command line arguments.
     */
    public void run(String[] args) throws Exception {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            LOG.error("Error parsing command line. {}", e.getMessage(), e);
            System.out.println("Example: \n" + parser.printExample(ALL));
            return;
        }
        showResourceFile("/banner.txt");
        // load Spring
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        this.appConfig = ctx.getBean(AppConfig.class);
        //System.out.println(Arrays.asList(new File(this.appConfig.postsPath()).list()));
        //System.out.println("-> " + this.appConfig.postsPath());
        //this.appConfig.setBaseDir(""); // scan sample
        this.jssgApp = ctx.getBean(JssgService.class);
        long t = System.currentTimeMillis();
        // init the directory structure
        if (this.init) {
            this.jssgApp.init();
            return;
        }

        // Generate the website
        if (this.build) {
            this.jssgApp.generate(this.backup);
        }
        System.out.printf("\nSite generated in %.2f sec\n", (System.currentTimeMillis() - t) / 1000.0);
        if (this.tree) {
            showBuild();
        }
        if (this.serve) {
            runJettyRun();
        }
    }

    /**
     * Serve the static web site in `_build`.
     */
    void runJettyRun() {
        try {
            System.out.println("\nStarting the Jetty server at http://localhost:" + this.port + " ...\n");
            Server server = new Server();
            Connector con = new SelectChannelConnector();
            con.setPort(this.port);
            server.addConnector(con);
            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setWelcomeFiles(new String[] {"index.html"});
            resource_handler.setResourceBase(this.appConfig.buildPath());
            HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[] {
                    resource_handler, new DefaultHandler()});
            server.setHandler(handlers);
            server.start();
            server.join();
        } catch (Exception e) {
            LOG.error("Error running Jetty.", e);
        }
    }

    public void showBuild() {
        System.out.println("\nThis is the generated build directory");
        System.out.println(DirectoryTree.directoryTree(new File(this.appConfig.buildPath())));
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        final App app = new App();
        try {
            app.run(args);
        } catch (Throwable e) {
            LOG.error("IM 'N UR PROGRAM, THROWIN' XCEPTIONZ", e);
            app.showResourceFile("/duke.txt"); // because it's fun
        }
    }
}
