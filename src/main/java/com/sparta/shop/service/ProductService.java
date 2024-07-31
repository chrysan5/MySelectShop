package com.sparta.shop.service;

import com.sparta.shop.dto.ItemDto;
import com.sparta.shop.dto.ProductMypriceRequestDto;
import com.sparta.shop.dto.ProductRequestDto;
import com.sparta.shop.dto.ProductResponseDto;
import com.sparta.shop.exception.ProductNotFoundException;
import com.sparta.shop.model.*;
import com.sparta.shop.repository.FolderRepository;
import com.sparta.shop.repository.ProductFolderRepository;
import com.sparta.shop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;


@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;
    private final MessageSource messageSource;

    public static final int MIN_MY_PRICE = 100;


    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = productRepository.save(new Product(requestDto, user));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductMypriceRequestDto requestDto) { //requestDto에 myprice 들어있음, id는 product의 아이디이다.
        int myprice = requestDto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            //throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + " 원 이상으로 설정해 주세요.");
            throw new IllegalArgumentException(
                    messageSource.getMessage(
                            "below.min.my.price",
                            new Integer[]{MIN_MY_PRICE},//매개변수를 사용할 경우 전달하는 값. messages.properties에 0번 인덱스로 전달하는 값. 배열타입으로 넘겨준다
                            "Wrong Price", //code에서 찾지못할 경우 이 default message가 전달된다.
                            Locale.getDefault() //언어를 번역해주는 역할을 함
                    )
            );
        }

        //Product product = productRepository.findById(productId)
        //.orElseThrow(() -> new NullPointerException("해당 아이디가 존재하지 않습니다."));
        
        Product product = productRepository.findById(productId).orElseThrow(() -> 
                new ProductNotFoundException(messageSource.getMessage(
                    "not.found.product",
                        null, //messages.properties에 보낼 매개변수 없으므로
                        "Not Found Product",
                        Locale.getDefault()
                ))
        );

       // product.setMyprice(myprice);
        //productRepository.save(product); //save할경우 이미 있는 객체면 update된다.대신 @Transactional 써도됨

        product.update(requestDto); //@Transactional해줘야 더티체킹이 된다.
        return new ProductResponseDto(product); //이때 ProductResponseDto의 생성자가 필요하다(이 경우 @AllArgsConstructor)
    }


    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리 //3줄 다 내장함수로, 셋트이다. 걍 적용 시키기
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> productList;

        // 사용자 권한 가져와서 ADMIN 이면 전체 조회, USER 면 본인이 추가한 부분 조회
        if (userRoleEnum == UserRoleEnum.USER) {
            productList = productRepository.findAllByUser(user, pageable);
        } else {
            productList = productRepository.findAll(pageable);
        }

        return productList.map(ProductResponseDto::new);
    }


    public void addFolder(Long productId, Long folderId, User user) {

        // 1) 상품을 조회합니다.
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new NullPointerException("해당 상품이 존재하지 않습니다.")
        );

        // 2) 폴더를 조회합니다.
        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new NullPointerException("해당 폴더가 존재하지 않습니다.")
        );

        // 3) 조회한 폴더와 상품이 모두 로그인한 회원의 소유인지 확인합니다.
        if (!product.getUser().getId().equals(user.getId())
                || !folder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }

        // 중복확인
        Optional<ProductFolder> overlapFolder = productFolderRepository.findByProductAndFolder(product, folder);

        if (overlapFolder.isPresent()) {
            throw new IllegalArgumentException("중복된 폴더입니다.");
        }

        // 4) 상품에 폴더를 추가합니다.
        productFolderRepository.save(new ProductFolder(product, folder));
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
        );
        product.updateByItemDto(itemDto);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProductsInFolder(Long folderId, int page, int size, String sortBy, boolean isAsc, User user) {

        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // 해당 폴더에 등록된 상품을 가져옵니다.
        Page<Product> products = productRepository.findAllByUserAndProductFolderList_FolderId(user, folderId, pageable);

        Page<ProductResponseDto> responseDtoList = products.map(ProductResponseDto::new);

        return responseDtoList;
    }
}


