package model.dto;

import lombok.Builder;

@Builder
public record CreateCustomerDto(
        String name,
        String email,
        String password
) {
}
