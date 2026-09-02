package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    public List<Category> getAllCategories() { return DatabaseManager.getAllCategories(); }

    public List<Category> getCategoriesBySubjectId (int subjectId) {
        List<Category> categories = new ArrayList<>();
        for (Category category : DatabaseManager.getAllCategories()) {
            if (category.getSubjectID() == subjectId) {
                categories.add(category);
            }
        }
        return categories;
    }

   public static Category getCategoryById(int id) {
       for (Category category : DatabaseManager.getAllCategories()) {
           if (category.getCategoryID() == id) {
               return category;
           }
       }
       return null;
   }
}
