package com.api.challenge.apichallenge.pagination;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@JsonIgnoreProperties(value = {
        "sort",
        "last",
        "first",
        "size",
        "number",
        "empty",
        "totalElements"
})
public class CustomPageImpl<T> extends PageImpl<T> {
    @Override
    public Pageable nextOrLastPageable() {
        return super.nextOrLastPageable();
    }

    @Override
    public Pageable previousOrFirstPageable() {
        return super.previousOrFirstPageable();
    }

    @Override
    public Stream<T> stream() {
        return super.stream();
    }

    @Override
    public <R> Streamable<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return super.flatMap(mapper);
    }

    @Override
    public Streamable<T> filter(Predicate<? super T> predicate) {
        return super.filter(predicate);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public Streamable<T> and(Supplier<? extends Stream<? extends T>> stream) {
        return super.and(stream);
    }

    @Override
    public Streamable<T> and(T... others) {
        return super.and(others);
    }

    @Override
    public Streamable<T> and(Iterable<? extends T> iterable) {
        return super.and(iterable);
    }

    @Override
    public Streamable<T> and(Streamable<? extends T> streamable) {
        return super.and(streamable);
    }

    @Override
    public List<T> toList() {
        return super.toList();
    }

    @Override
    public Set<T> toSet() {
        return super.toSet();
    }

    @Override
    public Stream<T> get() {
        return super.get();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return super.spliterator();
    }

    public CustomPageImpl(List<T> content, CustomPageable pageable, long total) {
        super(content, pageable, total);
    }
}
