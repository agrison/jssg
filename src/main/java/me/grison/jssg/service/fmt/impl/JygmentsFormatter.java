package me.grison.jssg.service.fmt.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.grison.jssg.cfg.AppConfig;
import me.grison.jssg.domain.Code;
import me.grison.jssg.domain.CodeExtract;
import me.grison.jssg.ex.JssgException;
import me.grison.jssg.service.file.impl.PostFilenameService;
import me.grison.jssg.service.fmt.FormatterService;
import me.grison.jssg.service.generator.GeneratorService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.threecrickets.jygments.format.Formatter;
import com.threecrickets.jygments.grammar.Lexer;

/**
 * Formatter service implementation using Jygments.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class JygmentsFormatter implements FormatterService {
    private final static Logger LOG = LoggerFactory.getLogger(PostFilenameService.class);
    Matcher highlightMatcher = Pattern.compile("([{]%\\s*highlight\\s+(\\w+)\\s*%[}](.*?)[{]%\\s*endhighlight\\s*%[}])", Pattern.DOTALL).matcher("");
    @Autowired
    AppConfig appConfig;

    String codeToHtml(String code, String lang) {
        try {
            Lexer lexer = Lexer.getByName(lang);
            Formatter formatter = Formatter.getByName("html");
            StringWriter sw = new StringWriter();
            formatter.format(lexer.getTokens(code), sw);
            Document doc = Jsoup.parse(sw.toString());
            return doc.select("div").html().replaceAll("<pre>", "<pre class=\"highlight\">");
        } catch (Exception e) {
            LOG.debug("Error trying to call Jygments for code snippet with language `" + lang + "`. Will call Pygments instead.");
            return callPygments(code, lang);
        }
    }

    public CodeExtract extractCodes(String markup, GeneratorService generatorService) {
        List<Code> codes = new ArrayList<Code>();
        this.highlightMatcher = this.highlightMatcher.reset(markup);
        StringBuffer sb = new StringBuffer();
        while (this.highlightMatcher.find()) {
            String whole = this.highlightMatcher.group();
            String lang = this.highlightMatcher.group(2);
            String code = this.highlightMatcher.group(3);
            this.highlightMatcher.appendReplacement(sb, "");
            String uuid = "-*-*-" + UUID.randomUUID().toString() + "-*-*-";
            sb.append(generatorService.codify(uuid));
            codes.add(new Code(uuid, whole, code, lang, codeToHtml(code, lang)));
        }
        this.highlightMatcher.appendTail(sb);
        return new CodeExtract(sb.toString(), codes);
    }

    public String mergeCodes(String markup, List<Code> codes) {
        for (Code code : codes) {
            markup = markup.replace("<code>" + code.getId() + "</code>", code.getHtmlCode());
        }
        return markup;
    }

    String callPygments(String code, String lang) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec(new String[] {
                    this.appConfig.pygmentsPath(), "-l", lang, "-f", "html"});
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            os.append(code);
            os.close();
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            while ((line = input.readLine()) != null) {
                sb.append(line).append("\n");
            }
            input.close();
            //error.close();
            return extractCodeFromPygmentsOutput(sb.toString(), lang);
        } catch (Exception e) {
            throw new JssgException("Could not convert code with Pygments.", e);
        }
    }

    String extractCodeFromPygmentsOutput(String output, String lang) {
        return Jsoup.parse(output.toString()).select("div").html().replaceAll("<pre>", "<pre class=\"highlight " + lang + "\">");
    }
}
