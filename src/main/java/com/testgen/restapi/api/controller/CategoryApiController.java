package com.testgen.restapi.api.controller;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/category")
public class CategoryApiController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryApiController(CategoryService categoryService) { this.categoryService = categoryService; }

    @GetMapping("/by-subject-id")
    public ResponseEntity<List<Category>> getCategoriesBySubjectId(@RequestParam(required = false, defaultValue = "1") Integer id) {
        List<Category> categories = categoryService.getCategoriesBySubjectId(id);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
