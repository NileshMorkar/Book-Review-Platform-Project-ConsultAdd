package com.example.BookReview.util;

import com.example.BookReview.dto.response.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> classType) {

        List<V> data = page.getContent().stream().map(object -> new ModelMapper().map(object, classType)).collect(Collectors.toList());

        PageableResponse<V> pageableResponse = new PageableResponse<>();

        pageableResponse.setData(data);
        pageableResponse.setPageNumber(page.getNumber() + 1);
        pageableResponse.setTotalPages(page.getTotalPages());
        pageableResponse.setLastPage(page.isLast());
        pageableResponse.setPageSize(page.getNumberOfElements());
        pageableResponse.setTotalElements(page.getTotalElements());

        return pageableResponse;
    }
}
