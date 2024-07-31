package com.sparta.shop.repository;


import com.sparta.shop.model.Folder;
import com.sparta.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);
    List<Folder> findAllByUserAndNameIn(User user, List<String> folderNames);
    // List<String> names(names는 폴더이름을 의미) 안에 user 있는지 찾기
}

