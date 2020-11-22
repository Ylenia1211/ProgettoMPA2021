package model;

import java.util.Date;

public class Pet {
    private final Integer id;
    public static int counter = 0;
    private String name;
    //private PetRace type;
    private Date birthday;
    private Client client;

    public Pet() {
        this.id = ++counter;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //public PetRace getType() {  return type; }

   // public void setType(PetRace type) {this.type = type;}

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    //aggiungere prenotazioni visite

}
