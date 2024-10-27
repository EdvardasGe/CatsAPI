package com.edvardas.CatsAPI.controller

import com.edvardas.CatsAPI.model.Cat
import com.edvardas.CatsAPI.repository.CatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class CatControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    CatRepository catRepository

    def setup() {
        catRepository.deleteAll()
    }

    def "POST /cats should create a new cat"() {
        expect:
        mockMvc.perform(post("/cats")
                .contentType("application/json")
                .content('{"name":"Oki","breed":"Ocicat","age":4,"color":"Gray","dateOfBirth":"2024-10-02"}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value("Oki"))
                .andExpect(jsonPath('$.breed').value("Ocicat"))
    }

    def "GET /cats should return list of cats"() {
        given:
        catRepository.save(new Cat(name: "Mellow", breed: "Abyssinian", age: 3, color: "Orange", dateOfBirth: new Date()))

        expect:
        mockMvc.perform(get("/cats").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].name').value("Mellow"))
    }

    def "GET /cats/{id} should return cat by id"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Mariot", breed: "Persian", age: 6, color: "Yellow", dateOfBirth: new Date()))

        expect:
        mockMvc.perform(get("/cats/${cat.id}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value("Mariot"))
    }

    def "GET /cats/{id} should return not found if cat does not exist"() {
        expect:
        mockMvc.perform(get("/cats/999"))
                .andExpect(status().isNotFound())
    }

    def "PUT /cats/{id} should update an existing cat"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Falco", breed: "Maine", age: 1, color: "Brown", dateOfBirth: new Date()))

        expect:
        mockMvc.perform(put("/cats/${cat.id}")
                .contentType("application/json")
                .content('{"name":"Falco","breed":"Maine","age":1,"color":"Brown","dateOfBirth":"2024-10-05"}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.age').value(1))
    }

    def "PUT /cats/{id} should return not found if cat does not exist"() {
        expect:
        mockMvc.perform(put("/cats/999")
                .contentType("application/json")
                .content('{"name":"Mari","breed":"Bombay","age":7,"color":"Black","dateOfBirth":"2024-09-04"}'))
                .andExpect(status().isNotFound())
    }

    def "DELETE /cats/{id} should delete an existing cat"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Rex", breed: "Bobtail", age: 2, color: "Brown", dateOfBirth: new Date()))

        expect:
        mockMvc.perform(delete("/cats/${cat.id}"))
                .andExpect(status().isNoContent())
    }

    def "DELETE /cats/{id} should return not found if cat does not exist"() {
        expect:
        mockMvc.perform(delete("/cats/999"))
                .andExpect(status().isNotFound())
    }
}