package com.edvardas.CatsAPI.controller

import com.edvardas.CatsAPI.exception.CatNotFoundException
import com.edvardas.CatsAPI.model.Cat
import com.edvardas.CatsAPI.service.CatService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.data.domain.PageRequest
import spock.lang.Specification
import spock.lang.Subject

class CatControllerSpec extends Specification {

    CatService catService = Mock()

    @Subject
    CatController catController = new CatController(catService: catService)

    def "should create a new cat"() {
        given:
        Cat cat = new Cat(name: "Mellow", breed: "Abyssinian", age: 3, color: "Orange")
        Cat savedCat = new Cat(id: 1L, name: "Mellow", breed: "Abyssinian", age: 3, color: "Orange")

        catService.createCat(cat) >> savedCat

        when:
        ResponseEntity<Cat> response = catController.createCat(cat)

        then:
        response.statusCodeValue == 200
        response.body != null
        response.body.id == 1L
        response.body.name == "Mellow"
        response.body.breed == "Abyssinian"
    }

    def "should get all cats"() {
        given:
        Cat cat1 = new Cat(name: "Mariot", breed: "Persian", age: 6, color: "Yellow")
        List<Cat> catsList = [cat1]
        catService.getAllCats(_ as Pageable) >> catsList // Mock the service to return a list of cats

        when:
        ResponseEntity<List<Cat>> response = catController.getAllCats(PageRequest.of(0, 10))

        then:
        response.statusCodeValue == 200
        response.body.size() == 1
        response.body[0].name == "Mariot"
    }

    def "should get cat by id"() {
        given:
        Long catId = 1L
        Cat cat = new Cat(id: catId, name: "Falco", breed: "Maine", age: 1, color: "Brown")
        catService.getCatById(catId) >> cat // Mock the service to return the specific cat

        when:
        ResponseEntity<Cat> response = catController.getCatById(catId)

        then:
        response.statusCodeValue == 200
        response.body.id == catId
        response.body.name == "Falco"
    }

    def "should return not found when getting cat by id that does not exist"() {
        given:
        Long nonExistentCatId = 999L

        catService.getCatById(nonExistentCatId) >> { throw new CatNotFoundException("Cat not found") }

        when:
        ResponseEntity<Cat> response = catController.getCatById(nonExistentCatId)

        then:
        response.statusCodeValue == 404
        response.body == null
    }

    def "should update a cat"() {
        given:
        Long catId = 1L
        Cat existingCat = new Cat(id: catId, name: "Whiskers", breed: "Tabby", age: 3, color: "Brown")
        Cat updatedCat = new Cat(name: "Whiskers", breed: "Siamese", age: 4, color: "White")
        catService.updateCat(catId, updatedCat) >> existingCat.with { it.name = "Whiskers"; it.breed = "Siamese"; it.age = 4; it.color = "White"; it } // Mock the update

        when:
        ResponseEntity<Cat> response = catController.updateCat(catId, updatedCat)

        then:
        response.statusCodeValue == 200
        response.body.name == "Whiskers"
        response.body.breed == "Siamese"
    }

    def "should return not found when updating a cat that does not exist"() {
        given:
        Long catId = 999L
        Cat updatedCat = new Cat(name: "Mari", breed: "Bombay", age: 7, color: "Black")
        catService.updateCat(catId, updatedCat) >> { throw new CatNotFoundException("Cat not found") } // Mock to throw an exception

        when:
        ResponseEntity<Cat> response = catController.updateCat(catId, updatedCat)

        then:
        response.statusCodeValue == 404
    }

    def "should delete a cat"() {
        given:
        Long catId = 1L
        catService.deleteCat(catId) // Just mock the deletion

        when:
        ResponseEntity<Void> response = catController.deleteCat(catId)

        then:
        response.statusCodeValue == 204 // No content
    }

    def "should return not found when deleting a cat that does not exist"() {
        given:
        Long catId = 999L
        catService.deleteCat(catId) >> { throw new CatNotFoundException("Cat not found") } // Mock to throw an exception

        when:
        ResponseEntity<Void> response = catController.deleteCat(catId)

        then:
        response.statusCodeValue == 404
    }
}