package com.ie.CA6.Entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actor")
public class Actor {

    @Id
    private int id;
    private String name;
    private String birthDate;
    private String nationality;
    private String image;

    public int getId(){ return id; }
    public String getBirthday() { return birthDate; }
    public String getNationality() { return nationality; }
    public String getName() { return name; }
    public String getImage() { return image; }
}