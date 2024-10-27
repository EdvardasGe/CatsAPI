package com.edvardas.CatsAPI.controller;

import com.edvardas.CatsAPI.model.Cat;
import com.edvardas.CatsAPI.service.CatService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cats")
public class CatController {

    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @Operation(summary = "Create a new Cat")
    @PostMapping
    public ResponseEntity<Cat> createCat(@Valid @RequestBody Cat cat) {
        return ResponseEntity.ok(catService.createCat(cat));
    }

    @Operation(summary = "Get all cats")
    @GetMapping
    public ResponseEntity<Page<Cat>> getAllCats(Pageable pageable) {
        return ResponseEntity.ok(catService.getAllCats(pageable));
    }

    @Operation(summary = "Get cat by Id")
    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long id) {
        Cat cat = catService.getCatById(id);
        return cat != null ? ResponseEntity.ok(cat) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update a cat")
    @PutMapping("/{id}")
    public ResponseEntity<Cat> updateCat(@PathVariable Long id,@Valid @RequestBody Cat catDetails) {
        Cat updatedCat = catService.updateCat(id, catDetails);
        return updatedCat != null ? ResponseEntity.ok(updatedCat) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a cat")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCat(@PathVariable Long id) {
        catService.deleteCat(id);
        return ResponseEntity.noContent().build();
    }
}
