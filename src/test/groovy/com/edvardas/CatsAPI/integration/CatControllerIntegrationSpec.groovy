package com.edvardas.CatsAPI.integration

import com.edvardas.CatsAPI.constants.TestConstants
import com.edvardas.CatsAPI.model.Cat
import com.edvardas.CatsAPI.repository.CatRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf

@SpringBootTest
@ActiveProfiles("prod")
@AutoConfigureMockMvc
class CatControllerIntegrationSpec extends Specification {

    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName(TestConstants.DB_NAME)
            .withUsername(TestConstants.DB_USERNAME)
            .withPassword(TestConstants.DB_PASSWORD)

    static {
        postgresContainer.start()
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
        registry.add("spring.datasource.username", postgresContainer::getUsername)
        registry.add("spring.datasource.password", postgresContainer::getPassword)
    }

    @Autowired
    MockMvc mockMvc

    @Autowired
    CatRepository catRepository

    def setup() {
        catRepository.deleteAll()
    }

    @WithMockUser(username = TestConstants.USERNAME, password = TestConstants.PASSWORD)
    def "POST /cats should create a new cat"() {
        expect:
        mockMvc.perform(post("/cats")
                .with(csrf())
                .contentType("application/json")
                .content('{"name":"Oki","breed":"Ocicat","age":4,"color":"Gray","dateOfBirth":"2024-10-02"}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value("Oki"))
                .andExpect(jsonPath('$.breed').value("Ocicat"))
    }

    @WithMockUser(username = TestConstants.USERNAME, password = TestConstants.PASSWORD)
    def "GET /cats should return list of cats"() {
        given:
        catRepository.save(new Cat(name: "Mellow", breed: "Abyssinian", age: 3, color: "Orange", dateOfBirth: new Date()))

        expect:
        mockMvc.perform(get("/cats").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[0].name').value("Mellow"))
    }

    @WithMockUser(username = TestConstants.USERNAME, password = TestConstants.PASSWORD)
    def "GET /cats/{id} should return cat by id"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Mariot", breed: "Persian", age: 6, color: "Yellow", dateOfBirth: new Date()))

        expect:
        mockMvc.perform(get("/cats/${cat.id}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.name').value("Mariot"))
    }

    @WithMockUser(username = TestConstants.USERNAME, password = TestConstants.PASSWORD)
    def "GET /cats/{id} should return not found if cat does not exist"() {
        expect:
        mockMvc.perform(get("/cats/999"))
                .andExpect(status().isNotFound())
    }

    @WithMockUser(username = TestConstants.USERNAME, password = TestConstants.PASSWORD)
    def "PUT /cats/{id} should update an existing cat"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Falco", breed: "Maine", age: 1, color: "Brown", dateOfBirth: new Date()))

        expect:
        mockMvc.perform(put("/cats/${cat.id}")
                .with(csrf())
                .contentType("application/json")
                .content('{"name":"Falco","breed":"Maine","age":1,"color":"Brown","dateOfBirth":"2024-10-05"}'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.age').value(1))
    }

    @WithMockUser(username = TestConstants.USERNAME, password = TestConstants.PASSWORD)
    def "PUT /cats/{id} should return not found if cat does not exist"() {
        expect:
        mockMvc.perform(put("/cats/999")
                .with(csrf())
                .contentType("application/json")
                .content('{"name":"Mari","breed":"Bombay","age":7,"color":"Black","dateOfBirth":"2024-09-04"}'))
                .andExpect(status().isNotFound())
    }

    @WithMockUser(username = TestConstants.USERNAME, password = TestConstants.PASSWORD)
    def "DELETE /cats/{id} should delete an existing cat"() {
        given:
        Cat cat = catRepository.save(new Cat(name: "Rex", breed: "Bobtail", age: 2, color: "Brown", dateOfBirth: new Date()))

        expect:
        mockMvc.perform(delete("/cats/${cat.id}")
                .with(csrf()))
                .andExpect(status().isNoContent())
    }

    @WithMockUser(username = TestConstants.USERNAME, password = TestConstants.PASSWORD)
    def "DELETE /cats/{id} should return not found if cat does not exist"() {
        expect:
        mockMvc.perform(delete("/cats/999")
                .with(csrf()))
                .andExpect(status().isNotFound())
    }
}