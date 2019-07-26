/*
 * Copyright (c) 2019. Igor Zubanov ( igor.zubanov@gmail.com ).
 * All rights reserved.
 */

package ehi;

import ehi.message.controller.converter.StringToLocalDateTimeConverter;
import ehi.message.controller.converter.StringToPinEntryCapabilityConverter;
import ehi.message.controller.converter.StringToPosCapabilityConverter;
import ehi.message.controller.converter.StringToSchemeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import javax.annotation.PostConstruct;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    public SpringResourceTemplateResolver templateResolver;

    @PostConstruct
    public void init() {
        templateResolver.setCacheable(false);
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/ehi/message");
        registry.addViewController("/ehi").setViewName("redirect:/ehi/message");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToSchemeConverter());
        registry.addConverter(new StringToLocalDateTimeConverter());
        registry.addConverter(new StringToPosCapabilityConverter());
        registry.addConverter(new StringToPinEntryCapabilityConverter());
    }
}