package me.grison.jssg.service.fmt.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.grison.jssg.service.fmt.TocService;
import me.grison.jssg.service.generator.GeneratorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Toc service implementation.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class TocServiceImpl implements TocService {
    Matcher m = Pattern.compile("<h([1-9])(.*?)>(.*?)</h\\1>").matcher("");
    @Autowired()
    @Qualifier("textileGenerator")
    GeneratorService textileGenerator;

    /**
     * Sanitize the given html by discarding each, replacing spaces by hyphens, and removing each
     * character not in range [\w.-].
     * 
     * @param html the html
     * @return the html.
     */
    private String sanitize(String html) {
        return html.replaceAll("<.*?>", "").replaceAll("\\s", "-").replaceAll("[^\\w.-]", "");
    }

    /**
     * Repeat `*` as many times as the `level` parameter.
     * @param level the list level.
     */
    private String level(int level) {
        return new String(new char[level]).replace("\0", "*");
    }

    public String createToc(String html) {
        StringBuilder menu = new StringBuilder();
        this.m = this.m.reset(html);
        while (this.m.find()) {
            String sanitized = sanitize(this.m.group(3));
            menu.append(String.format("%s \"%s\":#%s\n", level(Integer.valueOf(this.m.group(1))), this.m.group(3), sanitized));
        }
        return this.textileGenerator.markupToHtml(menu.toString());
    }

    public String addTocToHtml(String html) {
        StringBuffer sb = new StringBuffer();
        this.m = this.m.reset(html);
        while (this.m.find()) {
            this.m.appendReplacement(sb, "");
            String sanitized = sanitize(this.m.group(3));
            sb.append(String.format("<h%s%s id=\"%s\">%s</h%s>", this.m.group(1), this.m.group(2), sanitized, this.m.group(3), this.m.group(1)));
        }
        this.m.appendTail(sb);
        return sb.toString();
    }
}
