package com.rentalcar.crud.rentalcarapi.controller;

import com.rentalcar.crud.rentalcarapi.dto.CardDTO;
import com.rentalcar.crud.rentalcarapi.dto.CategoryDTO;
import com.rentalcar.crud.rentalcarapi.entity.CategoryEntity;
import com.rentalcar.crud.rentalcarapi.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/v1/api/categories")
@CrossOrigin("http://localhost:4200/")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value="/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {

        List<CategoryDTO> resp = StreamSupport.stream(this.categoryService
                .findAllCategories().spliterator(), false)
                .map(categoryEntity -> CategoryDTO.toDTO(categoryEntity))
                .collect(Collectors.toList());

        return new ResponseEntity<List<CategoryDTO>>(resp, HttpStatus.OK);
    }

    @GetMapping(value="/getbyguid/{guid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryEntity> getByGuidCategory(@PathVariable String guid) {

        CategoryEntity category = this.categoryService.findCategoryByGuid(guid);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping(value="/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryEntity> addNewCategory(@RequestBody CategoryDTO categoryDTO) {

        CategoryEntity newCategory = this.categoryService.addNewCategory(categoryDTO.getName());

        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryEntity> updateCategory(@RequestBody CategoryDTO categoryDTO) {

        if(!StringUtils.hasText(categoryDTO.getGuid())) {
            throw new ValidationException("A categoria não pode ser nula ou vazia");
        }
        CategoryEntity newCategory = this.categoryService.updateCategory(categoryDTO.getGuid(), categoryDTO.getName());

        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @DeleteMapping(value = "{guid}")
    public ResponseEntity<CategoryEntity> deleteCategory(@PathVariable String guid) {

        CategoryEntity retrievedCategory = this.categoryService.deleteCategory(guid);
        return new ResponseEntity<>(retrievedCategory, HttpStatus.OK);
    }

    @GetMapping(value="/getAmountLateByCategory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CardDTO>> getAmountLateByCategory() {

        List<CardDTO> cardDTOList = this.categoryService.getAmountLateByCategory();

        return new ResponseEntity<>(cardDTOList, HttpStatus.OK);
    }
}
