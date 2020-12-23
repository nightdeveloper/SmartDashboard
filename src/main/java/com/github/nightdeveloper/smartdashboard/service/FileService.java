package com.github.nightdeveloper.smartdashboard.service;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class FileService {

    public OutputStream getFileOutputStream(String path) throws FileNotFoundException {
        return new FileOutputStream(path);
    }
}
