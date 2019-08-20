package me.aboullaite.moneytransfer.service;

import me.aboullaite.moneytransfer.interfaces.Base;
import me.aboullaite.moneytransfer.interfaces.repositories.PagedResult;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PagedResultImpl<T> implements PagedResult<T> {

    private final boolean hasMore;
    private final int pageNumber;
    private final int recordsPerPage;
    private final Collection<T> content;

    private PagedResultImpl(final int pageNumber, final int recordsPerPage, Deque<T> content) {
        Objects.requireNonNull(content, "Content cannot be null");
        ValidationService.validatePagination(pageNumber, recordsPerPage);
        ValidationService.validatePageableContentSize(content, recordsPerPage);

        this.pageNumber = pageNumber;
        this.recordsPerPage = recordsPerPage;
        this.hasMore = content.size() > recordsPerPage;
        if (this.hasMore) {
            content.removeLast();
        }
        this.content = Collections.unmodifiableCollection(content);
    }

    @Override
    public boolean hasMore() {
        return hasMore;
    }

    @Override
    public Collection<T> getContent() {
        return content;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    static <T> PagedResult<T> of(final int pageNumber, final int recordsPerPage, final Deque<T> content) {
        ValidationService.validatePagination(pageNumber, recordsPerPage);
        Objects.requireNonNull(content, "Content cannot be null");
        return new PagedResultImpl<>(pageNumber, recordsPerPage, content);
    }

    public static <T extends Base> PagedResult<T> from(final int pageNumber, final int recordsPerPage,
                                                       final Map<String, T> content) {
        ValidationService.validatePagination(pageNumber, recordsPerPage);
        Objects.requireNonNull(content, "Content cannot be null");
        final Deque<T> pagedContent = content.values().stream()
                .sorted(Comparator.comparing(T::getId))
                .skip(recordsPerPage * (pageNumber - 1))
                .limit(recordsPerPage + 1)
                .collect(Collectors.toCollection(LinkedList::new));
        return PagedResultImpl.of(pageNumber, recordsPerPage, pagedContent);
    }

    public static <T extends Base> PagedResult<T> from(final int pageNumber, final int recordsPerPage,
                                                               final Map<String, T> content, Predicate<T> predicate) {
        ValidationService.validatePagination(pageNumber, recordsPerPage);
        Objects.requireNonNull(content, "Content cannot be null");
        final Deque<T> pagedContent = content.values().stream()
                .filter(predicate)
                .sorted(Comparator.comparing(T::getId))
                .skip(recordsPerPage * (pageNumber - 1))
                .limit(recordsPerPage + 1)
                .collect(Collectors.toCollection(LinkedList::new));
        return PagedResultImpl.of(pageNumber, recordsPerPage, pagedContent);
    }

    @Override
    public String toString() {
        return "PagedResultImpl{" +
                "hasMore=" + hasMore +
                ", pageNumber=" + pageNumber +
                ", recordsPerPage=" + recordsPerPage +
                ", content=" + content +
                '}';
    }
}
