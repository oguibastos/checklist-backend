package com.rentalcar.crud.rentalcarapi.dto;

import com.rentalcar.crud.rentalcarapi.entity.ChecklistItemEntity;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Getter
public class ChecklistItemDTO {

    private String guid;

    @NotBlank(message = "Checklist item description cannot be either null or empty")
    private String description;

    @NotNull(message = "is completed is mandatory")
    private Boolean isCompleted;

    @NotNull(message = "deadline is mandatory")
    private LocalDate deadline;

    private LocalDate postDate;

    @NotBlank(message = "Category guid name cannot be either null or empty")
    private String category;

    public static ChecklistItemDTO toDTO(ChecklistItemEntity checklistItemEntity) {

        return ChecklistItemDTO.builder()
                .guid(checklistItemEntity.getGuid())
                .description(checklistItemEntity.getDescription())
                .deadline(checklistItemEntity.getDeadline())
                .isCompleted(checklistItemEntity.getIsCompleted())
                .category(checklistItemEntity.getCategory().getGuid())
                .build();
    }
}
