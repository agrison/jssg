package me.grison.jssg.service.fmt;

import java.util.List;

import me.grison.jssg.domain.Code;
import me.grison.jssg.domain.CodeExtract;
import me.grison.jssg.service.generator.GeneratorService;

/**
 * Formatter Service.
 *
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public interface FormatterService {
    /**
     * Extract codes snippets in the given markup.
     * 
     * @param markup the markup.
     * @return the code snippets.
     */
    CodeExtract extractCodes(String markup, GeneratorService generatorService);

    /**
     * Merge the given list of codes in the given markup.
     * 
     * @param markup the markup.
     * @param codes the codes.
     * @return the resulting markup
     */
    String mergeCodes(String markup, List<Code> codes);
}
