import java.util.Date;

public class Actor {
    private int id;
    private String name;
    private String birthday;
    private String nationality;

    public Actor(int _id, String _name, String _birthday, String _nationality) {
        this.id = _id;
        this.name = _name;
        this.birthday = _birthday;
        this.nationality = _nationality;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
