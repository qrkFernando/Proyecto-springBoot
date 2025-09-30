package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageTestController {
    
    @GetMapping("/test-images")
    public String testImages(Model model) {
        List<String> imageTests = new ArrayList<>();
        
        String[] imageNames = {
            "el-quijote.jpg", "harry-potter.jpg", "codigo-da-vinci.jpg",
            "1984.jpg", "cien-anos-soledad.jpg", "el-alquimista.jpg"
        };
        
        for (String imageName : imageNames) {
            try {
                ClassPathResource resource = new ClassPathResource("static/images/" + imageName);
                if (resource.exists()) {
                    long size = resource.contentLength();
                    imageTests.add("✅ " + imageName + " - EXISTE (" + size + " bytes)");
                } else {
                    imageTests.add("❌ " + imageName + " - NO EXISTE");
                }
            } catch (IOException e) {
                imageTests.add("❌ " + imageName + " - ERROR: " + e.getMessage());
            }
        }
        
        model.addAttribute("imageTests", imageTests);
        return "image-test";
    }
}