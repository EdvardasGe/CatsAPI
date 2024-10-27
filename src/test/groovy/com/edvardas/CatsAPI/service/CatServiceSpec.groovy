package com.edvardas.CatsAPI.service

import com.edvardas.CatsAPI.exception.CatNotFoundException
import com.edvardas.CatsAPI.model.Cat
import com.edvardas.CatsAPI.repository.CatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneOffset

@SpringBootTest
@Transactional
class CatServiceSpec extends Specification {

    @Autowired
    CatService catService

    @Autowired
    CatRepository catRepository

    def "should create a cat"() {
        given:
        Cat cat = new Cat(name: "Mellow", breed: "Abyssinian", age: 3, color: "Orange",
                dateOfBirth: Date.from(LocalDate.of(2024, 9, 14).atStartOfDay().toInstant(ZoneOffset.UTC)))

        when:
        Cat result = catService.createCat(cat)

        then:
        result.id != null
        result.name == "Mellow"
        result.breed == "Abyssinian"
    }

    def "should get all cats"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Mariot", breed: "Persian", age: 6, color: "Yellow",
                dateOfBirth: Date.from(LocalDate.of(2022, 5, 4).atStartOfDay().toInstant(ZoneOffset.UTC))))

        when:
        List<Cat> result = catService.getAllCats(PageRequest.of(0, 10))

        then:
        result.size() == 1
        result[0].name == "Mariot"
    }

    def "should get cat by id"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Falco", breed: "Maine", age: 1, color: "Brown",
                dateOfBirth: Date.from(LocalDate.of(2020, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC))))

        when:
        Cat result = catService.getCatById(cat.id)

        then:
        result.id == cat.id
        result.name == "Falco"
    }

    def "should throw CatNotFoundException when getting cat by id that does not exist"() {
        when:
        catService.getCatById(999L)

        then:
        thrown(CatNotFoundException)
    }

    def "should update a cat"() {
        given:
        Cat existingCat = catRepository.save(new Cat(name: "Whiskers", breed: "Tabby", age: 3, color: "Brown",
                dateOfBirth: Date.from(LocalDate.of(2020, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC))))
        Cat updatedCat = new Cat(name: "Whiskers", breed: "Siamese", age: 4, color: "White",
                dateOfBirth: Date.from(LocalDate.of(2019, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC)))

        when:
        Cat result = catService.updateCat(existingCat.id, updatedCat)

        then:
        result.breed == "Siamese"
        result.age == 4
        result.color == "White"
    }

    def "should throw CatNotFoundException when updating a cat that does not exist"() {
        given:
        Cat updatedCat = new Cat(name: "Mari","breed":"Bombay","age":7,"color":"Black",
                dateOfBirth: Date.from(LocalDate.of(2019, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC)))

        when:
        catService.updateCat(999L, updatedCat)

        then:
        thrown(CatNotFoundException)
    }

    def "should delete a cat"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Rex", breed: "Bobtail", age: 2, color: "Brown",
                dateOfBirth: Date.from(LocalDate.of(2021, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC))))

        when:
        catService.deleteCat(cat.id)

        then:
        !catRepository.existsById(cat.id)
    }

    def "should throw CatNotFoundException when deleting a cat that does not exist"() {
        when:
        catService.deleteCat(999L)

        then:
        thrown(CatNotFoundException)
    }
}