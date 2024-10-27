package com.edvardas.CatsAPI.service;

import com.edvardas.CatsAPI.exception.CatNotFoundException;
import com.edvardas.CatsAPI.model.Cat;
import com.edvardas.CatsAPI.repository.CatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CatService {

    private final CatRepository catRepository;

    @Autowired
    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    public Cat createCat(Cat cat) {
        return catRepository.save(cat);
    }

    public List<Cat> getAllCats(Pageable pageable) {
        return catRepository.findAll(pageable).getContent();
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
        Cat cat = catRepository.findById(id)
                .orElseThrow(() -> new CatNotFoundException("Cat not found with id " + id));
        catRepository.delete(cat);
    }
}