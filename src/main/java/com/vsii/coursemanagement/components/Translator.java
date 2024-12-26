package com.vsii.coursemanagement.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
/**
 * class Translator use tp retrieve message from file properties based on message code
 * This class will use the currently language (locale) of system.
 * Class support multilingual for system (i18n), allow return elective  depending on the selected language
 */
@Component
public class Translator {

    // read files properties or yaml
    private static ResourceBundleMessageSource messageSource;

    /**
     *  Using constructor to transfer into object
     * @param messageSource receive message
     */
    private Translator(@Autowired ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }


    /**
     * Retrieve from file properties based on message code, and it's use the currently language of the selected system
     * @param msgCode - message
     * @return message returned depending on the language selected if not returned null
     * If not selected so the default is US
     */
    public static String toLocale(String msgCode, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, args, locale);
    }
}