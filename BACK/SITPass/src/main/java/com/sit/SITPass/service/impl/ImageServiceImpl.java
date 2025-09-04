package com.sit.SITPass.service.impl;

import com.sit.SITPass.model.Image;
import com.sit.SITPass.repository.ImageRepository;
import com.sit.SITPass.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void updateImage(Image image) {
        imageRepository.save(image);
    }

    @Transactional
    @Override
    public void deleteImageByPath(String path) {
        imageRepository.deleteImageByPath(path);
    }

    @Transactional
    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }
}
