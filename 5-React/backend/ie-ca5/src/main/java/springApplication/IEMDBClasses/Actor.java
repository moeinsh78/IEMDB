package springApplication.IEMDBClasses;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Actor {
    private int id;
    private String name;
    private String birthDate;
    private String nationality;
    private String image;

    public int getId(){
        return id;
    }

    public String getBirthday() { return birthDate; }

    public String getNationality() { return nationality; }

    public String getName(){
        return name;
    }

    public String getImage() {return image;}
}