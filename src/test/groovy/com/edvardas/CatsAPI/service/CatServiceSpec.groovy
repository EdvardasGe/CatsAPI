package com.edvardas.CatsAPI.service

import com.edvardas.CatsAPI.exception.CatNotFoundException
import com.edvardas.CatsAPI.model.Cat
import com.edvardas.CatsAPI.repository.CatRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.ZoneOffset

class CatServiceSpec extends Specification {

    CatRepository catRepository = Mock(CatRepository)

    @Subject
    CatService catService = new CatService(catRepository)

    def "should create a cat"() {
        given:
        Cat cat = new Cat(name: "Mellow", breed: "Abyssinian", age: 3, color: "Orange")

        when:
        Cat result = catService.createCat(cat)

        then:
        1 * catRepository.save(cat) >> { Cat c -> c.id = 1L; return c }  // Simulate save operation
        result.id != null
        result.name == "Mellow"
    }

    def "should get all cats"() {
        given:
        Cat cat = new Cat(name: "Mariot", breed: "Persian", age: 6, color: "Yellow",
                dateOfBirth: Date.from(LocalDate.of(2022, 5, 4).atStartOfDay().toInstant(ZoneOffset.UTC)))
        List<Cat> catsList = [cat]
        Page<Cat> catsPage = new PageImpl<>(catsList)

        catRepository.findAll(_) >> catsPage

        when:
        List<Cat> result = catService.getAllCats(PageRequest.of(0, 10))

        then:
        result.size() == 1
        result[0].name == "Mariot"
    }

    def "should get cat by id"() {
        given:
        Long catId = 1L
        Cat cat = new Cat(id: catId, name: "Falco", breed: "Maine", age: 1, color: "Brown")
        catRepository.findById(catId) >> Optional.of(cat)

        when:
        Cat result = catService.getCatById(catId)

        then:
        result.id == cat.id
        result.name == "Falco"
    }

    def "should throw CatNotFoundException when getting cat by id that does not exist"() {
        given:
        Long catId = 999L
        catRepository.findById(catId) >> Optional.empty()

        when:
        catService.getCatById(catId)

        then:
        thrown(CatNotFoundException)
    }

    def "should update a cat"() {
        given:
        Long catId = 1L
        Cat existingCat = new Cat(id: catId, name: "Whiskers", breed: "Tabby", age: 3, color: "Brown")
        Cat updatedCat = new Cat(name: "Whiskers", breed: "Siamese", age: 4, color: "White")

        catRepository.findById(catId) >> Optional.of(existingCat)
        catRepository.save(existingCat) >> existingCat

        when:
        Cat result = catService.updateCat(catId, updatedCat)

        then:
        result.breed == "Siamese"
        result.age == 4
        result.color == "White"
    }

    def "should throw CatNotFoundException when updating a cat that does not exist"() {
        given:
        Long catId = 999L
        Cat updatedCat = new Cat(name: "Mari", breed: "Bombay", age: 7, color: "Black")

        catRepository.findById(catId) >> Optional.empty()

        when:
        catService.updateCat(catId, updatedCat)

        then:
        thrown(CatNotFoundException)
    }

    def "should delete a cat"() {
        given:
        Long catId = 1L
        Cat cat = new Cat(id: catId, name: "Rex", breed: "Bobtail", age: 2, color: "Brown")

        catRepository.findById(catId) >> Optional.of(cat)

        when:
        catService.deleteCat(catId)

        then:
        1 * catRepository.delete(cat)
    }

    def "should throw CatNotFoundException when deleting a cat that does not exist"() {
        given:
        Long catId = 999L
        catRepository.findById(catId) >> Optional.empty()

        when:
        catService.deleteCat(catId)

        then:
        thrown(CatNotFoundException)
    }
}
