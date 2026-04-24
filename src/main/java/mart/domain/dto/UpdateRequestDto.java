package mart.domain.dto;

import mart.domain.entity.Status;

public record UpdateRequestDto(
    String name,
    String category,
    Integer quantity,
    Double price,
    Status status
) {}
