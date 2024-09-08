package com.transfer.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TransactionPageResponseDTO(
        List<TransactionResponseDTO> transactionsForAccount,

        Integer pageNumber,

        Integer pageSize,

        int totalElement,

        int totalPages,

        boolean isLast
) {

}
