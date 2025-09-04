package com.sit.SITPass.repository;

import com.sit.SITPass.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    void deleteImageByPath(String path);
}
