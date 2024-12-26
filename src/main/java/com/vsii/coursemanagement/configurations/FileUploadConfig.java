package com.vsii.coursemanagement.configurations;

import com.vsii.coursemanagement.utils.ConstantKey;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.util.unit.DataSize;

/**
 * This class marked configuration
 * Use to config the upload files not exceed 100MB
 */
@Configuration
public class FileUploadConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        factory.setMaxFileSize(DataSize.ofMegabytes(ConstantKey.MAX_FILE_SIZE_MB));
        factory.setMaxRequestSize(DataSize.ofMegabytes(ConstantKey.MAX_FILE_SIZE_MB));

        return factory.createMultipartConfig();
    }
}
