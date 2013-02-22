package me.grison.jssg.service.tpl.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import me.grison.jssg.cfg.AppConfig;
import me.grison.jssg.domain.Header;
import me.grison.jssg.domain.Context;
import me.grison.jssg.ex.JssgException;
import me.grison.jssg.service.fmt.TocService;
import me.grison.jssg.service.tpl.TemplateService;

import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Template service implementation using Freemarker.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class FreemarkerService implements TemplateService {
    @Autowired
    AppConfig appConfig;
    @Autowired
    TocService tocService;
    final Configuration cfg = new Configuration();

    public String generateHtml(String html, Header header, Context env) {
        if (header == null) {
            return html;
        }
        // retrieve the layout to be used
        Template tpl = getTemplate(header);
        Map<String, Object> fmEnv = new HashMap<String, Object>();
        fmEnv.put("header", header.getValues());
        if (header.hasToc()) {
            html = this.tocService.addTocToHtml(html);
            fmEnv.put("toc", this.tocService.createToc(html));
        }
        fmEnv.put("content", html);
        fmEnv.putAll(env.get());
        return processTemplate(tpl, fmEnv);
    }

    Template getTemplate(Header header) {
        try {
            return this.cfg.getTemplate(this.appConfig.layoutPath() + header.getLayout() + ".html");
        } catch (Exception e) {
            throw new JssgException("Could not locate template `" + header.getLayout() + "`.", e);
        }
    }

    String processTemplate(Template template, Map<?, ?> env) {
        try {
            StringWriter sw = new StringWriter();
            template.process(env, sw);
            return sw.toString();
        } catch (Exception e) {
            throw new JssgException("Could not process template `" + template + "`.", e);
        }
    }
}
