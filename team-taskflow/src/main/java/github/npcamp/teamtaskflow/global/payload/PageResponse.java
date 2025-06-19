package github.npcamp.teamtaskflow.global.payload;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;

    public PageResponse(Page<T> page){
        content = page.getContent();
        totalElements = page.getTotalElements();
        totalPages = page.getTotalPages();
        size = page.getSize();
        number = page.getNumber();
    }
}