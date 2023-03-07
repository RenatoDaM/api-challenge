package com.api.challenge.apichallenge.pagination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
@JsonIgnoreProperties(value = {
        "sort",
        "last",
        "first",
        "size",
        "number",
        "empty",
        "totalElements"
})

// APENAS OUTRA POSSÍVEL FORMA DE IMPLEMENTAÇÃO. DEIXADA DE EXEMPLO.

public class CustomPage<T> implements Page<T> {
    private final List<T> content;
    private final CustomPageable pageable;
    private final long totalElements;

    public CustomPage(List<T> content, CustomPageable pageable, long totalElements) {
        this.content = content;
        this.pageable = pageable;
        this.totalElements = totalElements;
    }

    @Override
    public int getTotalPages() {
        int pageSize = getSize();
        long totalElements = getTotalElements();
        int totalPages = (int) Math.ceil((double) totalElements / (double) pageSize);
        return Math.max(1, totalPages);
    }

    @Override
    public long getTotalElements() {
        return totalElements;
    }

    @Override
    public Page map(Function converter) {
        return null;
    }

    @Override
    public int getNumber() {
        return pageable.getPageNumber();
    }

    @Override
    public int getSize() {
        return pageable.getPageSize();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public boolean hasNext() {
        return getNumber() < getTotalPages() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        if (hasPrevious()) {
            return pageable.previousOrFirst();
        }
        return Pageable.unpaged();
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
