package com.edvardas.CatsAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
public class Cat {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @Column
    @NotNull
    @Size(min = 1, max = 50)
    private String breed;

    @Column
    @NotNull
    @Min(0)
    private Integer age;

    @Column
    @NotNull
    @Size(min = 1, max = 20)
    private String color;

    @Column
    @NotNull
    @Past
    private Date dateOfBirth;

    public Cat() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public Integer  getAge() {
        return age;
    }

    public String getColor() {
        return color;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setAge(Integer  age) {
        this.age = age;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
