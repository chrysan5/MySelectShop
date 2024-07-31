package com.sparta.shop.controller;


import com.sparta.shop.dto.ItemDto;
import com.sparta.shop.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemSearchController {

    private final ItemSearchService itemSearchService;

    @Autowired
    public ItemSearchController(ItemSearchService itemSearchService) {
        this.itemSearchService = itemSearchService;
    }

    //키워드로 상품검색
    @GetMapping("/api/search")
    @ResponseBody
    public List<ItemDto> searchItems(@RequestParam("query") String query) {
        return itemSearchService.searchItems(query);
    }
}