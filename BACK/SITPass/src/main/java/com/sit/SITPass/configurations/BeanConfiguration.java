package com.sit.SITPass.configurations;

import java.io.IOException;
import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public LanguageDetector languageDetector() throws Exception {
        LanguageDetector languageDetector;
        try {
            languageDetector = LanguageDetector.getDefaultLanguageDetector().loadModels();
        } catch (IOException e) {
            throw new Exception("Error while loading language models.");
        }
        return languageDetector;
    }
}

