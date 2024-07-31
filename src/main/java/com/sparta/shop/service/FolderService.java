package com.sparta.shop.service;

import com.sparta.shop.dto.FolderResponseDto;
import com.sparta.shop.model.Folder;
import com.sparta.shop.model.User;
import com.sparta.shop.repository.FolderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;


    //@Transactional을 붙이면 aop가 동작하는 것이다!
    //@Transactional 붙인거와 아래거와 같음 (구버전)
/*    public List<Folder> addFolders(List<String> folderNames, User user) {
        // 트랜잭션의 시작
        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // 1) 입력으로 들어온 폴더 이름을 기준으로, 회원이 이미 생성한 폴더들을 조회합니다.
            List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames);

            List<Folder> savedFolderList = new ArrayList<>();
            for (String folderName : folderNames) {
                // 2) 이미 생성한 폴더가 아닌 경우만 폴더 생성
                if (isExistFolderName(folderName, existFolderList)) {
                    // Exception 발생!
                    throw new IllegalArgumentException("중복된 폴더명을 제거해 주세요! 폴더명: " + folderName);
                } else {
                    Folder folder = new Folder(folderName, user);
                    // 폴더명 저장
                    folder = folderRepository.save(folder);
                    savedFolderList.add(folder);
                }
            }

            // 트랜잭션 commit
            transactionManager.commit(status);

            return savedFolderList;
        } catch (Exception ex) {
            // 트랜잭션 rollback
            transactionManager.rollback(status);
            throw ex;
        }
    }*/


    // 로그인한 회원에 폴더들 등록
    public void addFolders(List<String> folderNames, User user) {

        // 입력으로 들어온 폴더 이름을 기준으로, 회원이 이미 생성한 폴더들을 조회합니다.
        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user, folderNames);

        List<Folder> folderList = new ArrayList<>(); //결과를 담을 리스트

        for (String folderName : folderNames) {
            // 이미 생성한 폴더가 아닌 경우만 폴더 생성
            if (!isExistFolderName(folderName, existFolderList)) {
                Folder folder = new Folder(folderName, user);
                folderList.add(folder);
            } else {
                //throw new IllegalArgumentException("폴더명이 중복되었습니다.");
                throw new IllegalArgumentException("중복된 폴더명을 제거해주세요! 폴더명: " + folderName);
            }
        }

        folderRepository.saveAll(folderList);
    }

    private Boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        // 기존 폴더 리스트에서 folder name 이 있는지?
        for (Folder existFolder : existFolderList) {
            if(folderName.equals(existFolder.getName())) {
                return true;
            }
        }
        return false;
    }


    // 로그인한 회원이 등록된 모든 폴더 조회
    public List<FolderResponseDto> getFolders(User user) {
        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> responseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            responseDtoList.add(new FolderResponseDto(folder));
        }

        return responseDtoList;
    }

}