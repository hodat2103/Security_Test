package com.vsii.coursemanagement.configurations;

import com.vsii.coursemanagement.utils.ConstantKey;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.util.unit.DataSize;

/**
 * duoc danh dau la lop cau hinh
 * dung de config cac file upload khong vuot qua 100MB
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
