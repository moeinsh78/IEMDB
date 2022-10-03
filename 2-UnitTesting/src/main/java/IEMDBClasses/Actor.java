package IEMDBClasses;

public class Actor {
    private int id;
    private String name;
    private String birthDate;
    private String nationality;

    public int getId(){
        return id;
    }

    public String getBirthday() { return birthDate; }

    public String getNationality() { return nationality; }

    public String getName(){
        return name;
    }
}