package com.ie.CA7.Entity;


import javax.persistence.*;

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