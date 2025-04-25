package roukaya.chelly.callerapp;

public class Contact {
    int id ;
    String firstname,lastname,phone;
    public Contact(int id,String f,String l,String n) {
        this.id=id;
        this.firstname=f;
        this.lastname=l;
        this.phone=n;

    }
    public Contact(String f,String l,String n) {
        this.firstname=f;
        this.lastname=l;
        this.phone=n;

    }
}
