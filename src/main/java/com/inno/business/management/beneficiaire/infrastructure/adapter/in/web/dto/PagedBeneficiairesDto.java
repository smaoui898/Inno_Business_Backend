package com.inno.business.management.beneficiaire.infrastructure.adapter.in.web.dto;

import java.util.List;

public record PagedBeneficiairesDto(
        List<BeneficiaireResponseDto> content,
        long totalElements,
        int totalPages,
        int currentPage
) {
}
