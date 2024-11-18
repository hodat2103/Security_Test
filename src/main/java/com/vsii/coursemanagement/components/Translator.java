package com.vsii.coursemanagement.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
/**
 * class Translator dung de lay thong diep tu file properties dua tren ma  (message code)
 * va su dung ngon ngu hien tai (locale) cua he thong   .
 * Class ho tro da ngon ngu cho ung dung (i18n), cho phep tra ve tuy theo ngon ngu duoc lua chonj
 */
@Component
public class Translator {

    // doc cac file properties or yaml
    private static ResourceBundleMessageSource messageSource;

    /**
     *  su dung constructor de truyen vao doi tuong
     * @param messageSource nhan ma thong diep
     */
    private Translator(@Autowired ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }


    /**
     * lay thong diep tu file properties du tren ma message code va su dung ngon ngu hien tai cua he thong dax chonj
     * @param msgCode - ma thong diep
     * @return thong diep se tra ve theo ngon ngu da chon neu khong thi return null
     * khong chon thi mac dinh se la tieng anh
     */
    public static String toLocale(String msgCode, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage(msgCode, args, locale);
        return message;
    }
}