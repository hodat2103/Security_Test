package com.vsii.coursemanagement.services.implement;

import com.vsii.coursemanagement.components.Translator;
import com.vsii.coursemanagement.entities.Language;
import com.vsii.coursemanagement.exceptions.DataNotFoundException;
import com.vsii.coursemanagement.repositories.LanguageRepository;
import com.vsii.coursemanagement.services.ILanguageService;
import com.vsii.coursemanagement.utils.MessageKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Service tiep nhan cac cong viec tu IService nay xu ly cac logic lien quan den language
 */
@Service

public class LanguageService implements ILanguageService {

    @Autowired
    private LanguageRepository languageRepository;

    /**
     * method de lay ra tat ca cac ngon ngu trong bang du lieu language
     *
     * @return tra ve (http code, massage or ( data)) theo ket qua tuong ung
     */
    @Override
    public List<Language> getAllLanguages() throws DataNotFoundException {

            List<Language> languages = languageRepository.findAll();

            if (languages.isEmpty()) {
                // nem ra ex DataNotFoundException khi danh sach rong
                throw new DataNotFoundException(Translator.toLocale(MessageKey.NO_LANGUAGES_FOUND));
            }

            // tra ve danh sach co du lieu
            return languages;

    }
}
