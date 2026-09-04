package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.api.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> getAllCategories() { return categoryRepo.findAll(); }

    public List<Category> getCategoriesBySubjectId (int subjectId) {
        return categoryRepo.findBySubjectID(subjectId);
    }

   public Category getCategoryById(int id) {
       Optional<Category> category = categoryRepo.findById(id);
       return category.orElse(null);
   }
}
