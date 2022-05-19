package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.UserLanguage;
import com.gudratli.nsbtodoapi.exception.duplicate.DuplicateUserLanguageException;
import com.gudratli.nsbtodoapi.repository.LanguageRepository;
import com.gudratli.nsbtodoapi.repository.UserLanguageRepository;
import com.gudratli.nsbtodoapi.repository.UserRepository;
import com.gudratli.nsbtodoapi.service.inter.UserLanguageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserLanguageServiceImpl implements UserLanguageService
{
    private final UserLanguageRepository userLanguageRepository;
    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;

    public UserLanguageServiceImpl (UserLanguageRepository userLanguageRepository,
            UserRepository userRepository, LanguageRepository languageRepository)
    {
        this.userLanguageRepository = userLanguageRepository;
        this.userRepository = userRepository;
        this.languageRepository = languageRepository;
    }

    @Override
    public List<UserLanguage> getAll ()
    {
        return userLanguageRepository.findAll();
    }

    @Override
    public List<UserLanguage> getByUserId (Integer id)
    {
        return userLanguageRepository.findByUser(userRepository.findById(id).orElse(null));
    }

    @Override
    public List<UserLanguage> getByLanguageId (Integer id)
    {
        return userLanguageRepository.findByLanguage(languageRepository.findById(id).orElse(null));
    }

    @Override
    public UserLanguage getById (Integer id)
    {
        return userLanguageRepository.findById(id).orElse(null);
    }

    @Override
    public UserLanguage add (UserLanguage userLanguage) throws DuplicateUserLanguageException
    {
        checkForDuplicate(userLanguage);

        return userLanguageRepository.save(userLanguage);
    }

    @Override
    public UserLanguage update (UserLanguage userLanguage) throws DuplicateUserLanguageException
    {
        checkForDuplicate(userLanguage);

        return userLanguageRepository.save(userLanguage);
    }

    @Override
    public void remove (Integer id)
    {
        userLanguageRepository.findById(id).ifPresent(userLanguageRepository::delete);
    }

    private void checkForDuplicate (UserLanguage userLanguage) throws DuplicateUserLanguageException
    {
        UserLanguage test = userLanguageRepository.findByUserAndLanguage(userLanguage.getUser(),
                userLanguage.getLanguage());

        if (test != null && !test.getId().equals(userLanguage.getId()))
            throw new DuplicateUserLanguageException();
    }
}
