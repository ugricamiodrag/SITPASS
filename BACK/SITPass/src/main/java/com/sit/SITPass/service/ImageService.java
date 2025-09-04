package com.sit.SITPass.service;

import com.sit.SITPass.model.Image;
import org.springframework.stereotype.Service;

@Service
public interface ImageService {

    void updateImage(Image image);

    void deleteImageByPath(String path);

    Image saveImage(Image image);
}
