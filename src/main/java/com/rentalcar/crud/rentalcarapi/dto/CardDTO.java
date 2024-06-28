package com.rentalcar.crud.rentalcarapi.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CardDTO {
    private String categoryName;
    private int amountItemsLate;
}
