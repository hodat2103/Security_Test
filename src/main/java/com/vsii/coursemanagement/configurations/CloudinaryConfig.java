package com.vsii.coursemanagement.configurations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class marked is Configuration used to config cloudinary (add dependency in file pom.xml)
 * Contains cloud_name: user's code in the cloudinary
 *          api_key
 *          api_secret: APIs to verify the access permission to user's cloudinary
 */
@Configuration
public class CloudinaryConfig {
    private final String CLOUD_NAME = "dln3udzvm";
    private final String API_KEY = "413691979578788";
    private final String API_SECRET = "Dg6X56CPDjlU9pNRXvty1tNWBLU";

    /**
     * Transfer into information to access the cloudinary
     * @return
     */
    public Cloudinary cloudinary(){
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name",CLOUD_NAME,
                "api_key",API_KEY,
                "api_secret",API_SECRET,
                "secure", true

        ));
        return cloudinary;
    }
}
