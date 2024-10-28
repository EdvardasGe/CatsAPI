package com.edvardas.CatsAPI.controller;

import com.edvardas.CatsAPI.exception.CatNotFoundException;
import com.edvardas.CatsAPI.model.Cat;
import com.edvardas.CatsAPI.service.CatService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cats")
public class CatController {

    @Autowired
    private CatService catService;

    @Operation(summary = "Create a new Cat")
    @PostMapping
    public ResponseEntity<Cat> createCat(@Valid @RequestBody Cat cat) {
        return ResponseEntity.ok(catService.createCat(cat));
    }

    @Operation(summary = "Get all cats")
    @GetMapping
    public ResponseEntity<List<Cat>> getAllCats(Pageable pageable) {
        return ResponseEntity.ok(catService.getAllCats(pageable));
    }

    @Operation(summary = "Get cat by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long id) {
        try {
            Cat cat = catService.getCatById(id);
            return ResponseEntity.ok(cat);
        } catch (CatNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update a cat")
    @PutMapping("/{id}")
    public ResponseEntity<Cat> updateCat(@PathVariable Long id, @Valid @RequestBody Cat catDetails) {
        try {
            Cat updatedCat = catService.updateCat(id, catDetails);
            return ResponseEntity.ok(updatedCat);
        } catch (CatNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a cat")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long id) {
        try {
            catService.deleteCat(id);
            return ResponseEntity.noContent().build();
        } catch (CatNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
