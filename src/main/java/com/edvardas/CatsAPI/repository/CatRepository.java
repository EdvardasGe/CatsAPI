package com.edvardas.CatsAPI.repository;


import com.edvardas.CatsAPI.model.Cat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatRepository extends JpaRepository<Cat, Long> {
}
