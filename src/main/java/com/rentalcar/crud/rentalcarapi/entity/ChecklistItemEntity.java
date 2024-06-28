package com.rentalcar.crud.rentalcarapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Data
@Entity(name = "ChecklistItem")
@Table(indexes = { @Index(name = "IDX_GUID_CK_IT", columnList = "guid")})
public class ChecklistItemEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checklistItemId;

    private String description;

    private Boolean isCompleted;

    private LocalDate deadline;

    private LocalDate postedDate;

    @ManyToOne
    private CategoryEntity category;

    @Override
    public String toString() {
        return "ChecklistItemEntity{" +
                "checklistItemId=" + checklistItemId +
                ", description='" + description + '\'' +
                ", isCompleted=" + isCompleted +
                ", deadline=" + deadline +
                ", postedDate=" + postedDate +
                ", categoryId=" + (category != null ? category.getCategoryId() : null) +
                '}';
    }
}
