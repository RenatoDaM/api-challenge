package com.api.challenge.apichallenge.pagination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@JsonIgnoreProperties({
        "sort",
        "unpaged",
        "paged"
})
public class CustomPageable extends AbstractPageRequest {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    public CustomPageable(Integer page, Integer size) {
        super(page != null ? page : DEFAULT_PAGE, size != null ? size : DEFAULT_SIZE);    }

    @Override
    public boolean isPaged() {
        return super.isPaged();
    }

    @Override
    public boolean isUnpaged() {
        return super.isUnpaged();
    }

    @Override
    public Sort getSortOr(Sort sort) {
        return super.getSortOr(sort);
    }

    @Override
    public Optional<Pageable> toOptional() {
        return super.toOptional();
    }

    @Override
    public boolean hasPrevious() {
        return getPageNumber() > 0;
    }

    @Override
    public Pageable next() {
        return new CustomPageable(getPageNumber() + 1, getPageSize());
    }

    @Override
    public Pageable previous() {
        return new CustomPageable(getPageNumber() - 1, getPageSize()) ;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? new CustomPageable(getPageNumber() - 1, getPageSize()) : this;
    }

    @Override
    public Pageable first() {
        return new CustomPageable(0, getPageSize());
    }

    @Override
    public long getOffset() {
        return (long) getPageNumber() * (long) getPageSize();
    }

    @Override
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new CustomPageable(pageNumber, getPageSize());
    }

    @Override
    public int getPageSize() {
        int pageSize = super.getPageSize();
        return pageSize != 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    public int getNumberOfItemsOnPage(int totalItems) {
        int firstItemIndex = (getPageNumber() * getPageSize());
        int lastItemIndex = Math.min(firstItemIndex + getPageSize(), totalItems);
        return lastItemIndex - firstItemIndex;
    }
}
