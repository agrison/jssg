package me.grison.jssg.service.generator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.grison.jssg.domain.CodeExtract;
import me.grison.jssg.domain.Header;
import me.grison.jssg.domain.Context;
import me.grison.jssg.service.domain.HeaderService;
import me.grison.jssg.service.fmt.FormatterService;
import me.grison.jssg.service.tpl.TemplateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is the base generator class.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public abstract class BaseGenerator implements GeneratorService {
    private final static Logger LOG = LoggerFactory.getLogger(BaseGenerator.class);
    @Autowired
    HeaderService headerService;
    @Autowired
    TemplateService templateService;
    @Autowired
    FormatterService formatterService;

    public Header extractHeader(String markup) {
        return this.headerService.extractHeader(markup);
    }

    public String generateHtml(String markup, Context env) {
        Header header = extractHeader(markup);
        markup = this.headerService.removeHeader(markup, header);
        markup = replacePlaceHolders(markup, header);
        // replace code snippets in it
        CodeExtract codes = this.formatterService.extractCodes(markup, this);
        markup = codes.getMarkup();
        String html = markupToHtml(markup);
        html = this.formatterService.mergeCodes(html, codes.getSnippets());
        return this.templateService.generateHtml(html, header, env);
    }

    /**
     * Replace place holders ala {{foo}} with the value of `foo` in the header.
     * Place holders with value not found, won't be replaced.
     * 
     * @param str the string.
     * @param header the header information.
     * @return the string with place holders replaced.
     */
    public String replacePlaceHolders(String str, Header header) {
        if (header == null) {
            return str;
        }
        Matcher placeHolders = Pattern.compile("([{]\\s*[{](.*?)[}]\\s*[}])").matcher(str);
        Map<String, Object> env = header.getValues();
        StringBuffer sb = new StringBuffer();
        while (placeHolders.find()) {
            String original = placeHolders.group();
            String var = placeHolders.group(2).trim();
            placeHolders.appendReplacement(sb, "");
            if (env.containsKey(var)) {
                LOG.debug("Replacing placeholder `{}` with `{}`", original, env.get(var));
                sb.append(env.get(var));
            } else {
                sb.append(original);
            }
        }
        placeHolders.appendTail(sb);
        return sb.toString();
    }
}
