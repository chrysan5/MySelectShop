package com.sparta.shop.repository;

import com.sparta.shop.model.Folder;
import com.sparta.shop.model.Product;
import com.sparta.shop.model.ProductFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductFolderRepository extends JpaRepository<ProductFolder, Long> {
    Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);
}