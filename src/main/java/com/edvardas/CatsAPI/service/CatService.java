package com.edvardas.CatsAPI.service;

import com.edvardas.CatsAPI.exception.CatNotFoundException;
import com.edvardas.CatsAPI.model.Cat;
import com.edvardas.CatsAPI.repository.CatRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CatService {

    private final CatRepository catRepository;

    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    public Cat createCat(Cat cat) {
        return catRepository.save(cat);
    }

    public Page<Cat> getAllCats(Pageable pageable) {
        return catRepository.findAll(pageable);
    }

    public Cat getCatById(Long id) {
        return catRepository.findById(id).orElseThrow(() -> new CatNotFoundException("Cat not found with id: " + id));
    }

    public Cat updateCat(Long id, Cat updatedCat) {
        Cat cat = getCatById(id);
        cat.setName(updatedCat.getName());
        cat.setBreed(updatedCat.getBreed());
        cat.setAge(updatedCat.getAge());
        cat.setColor(updatedCat.getColor());
        cat.setDateOfBirth(updatedCat.getDateOfBirth());
        return catRepository.save(cat);
    }

    public void deleteCat(Long id) {
        if (!catRepository.existsById(id)) {
            throw new CatNotFoundException("Cat not found with id: " + id);
        }
        catRepository.deleteById(id);
    }
}