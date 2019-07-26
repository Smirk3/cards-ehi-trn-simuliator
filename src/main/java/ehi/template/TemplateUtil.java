/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi.template;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

public class TemplateUtil {

    public static Template findTemplate(List<Template> templates, String templateId) throws TemplateNotFoundException {
        if (!StringUtils.hasText(templateId) || CollectionUtils.isEmpty(templates)) {
            throw new TemplateNotFoundException();
        }

        Optional<Template> template = templates.stream().filter(c -> templateId.equals(c.id)).findAny();

        if (template == null || !template.isPresent()){
            throw new TemplateNotFoundException();
        }

        return template.get();
    }

}
