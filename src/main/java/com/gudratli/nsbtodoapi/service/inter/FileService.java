package com.gudratli.nsbtodoapi.service.inter;

import com.gudratli.nsbtodoapi.entity.File;

public interface FileService
{
    File getFileById (Integer id);

    File add (File file);
}
