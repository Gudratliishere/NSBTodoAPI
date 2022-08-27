package com.gudratli.nsbtodoapi.service.impl;

import com.gudratli.nsbtodoapi.entity.File;
import com.gudratli.nsbtodoapi.repository.FileRepository;
import com.gudratli.nsbtodoapi.service.inter.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService
{
    private final FileRepository fileRepository;

    @Override
    public File getFileById (Integer id)
    {
        return fileRepository.findById(id).orElse(null);
    }

    @Override
    public File add (File file)
    {
        return fileRepository.save(file);
    }
}
