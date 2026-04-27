package com.devstack.POS.dto.response;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PagedResponseDTO<T> {
    private long dataCount;
    private List<T> dataList;
}
