package com.testgen.restapi.api.service;

import com.testgen.restapi.api.model.Category;
import com.testgen.restapi.core.managers.DatabaseManager;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

   public static Category getCategoryById(int id) {
       for (Category category : DatabaseManager.getAllCategories()) {
           if (category.getCategoryID() == id) {
               return category;
           }
       }
       return null;
   }
}
