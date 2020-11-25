package model;


import java.util.Set;

public class Client extends Person {
    private String address;
    private String city;
    private String telephone;
    private String email;

    //private Set<Pet> pets;

    public Client(String name, String surname, String address, String city, String telephone, String email) {
        super();
        this.setFirstName(name);
        this.setLastName(surname);
        this.address = address;
        this.city = city;
        this.telephone = telephone;
        this.email = email;
    }

    public Client() {

    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return this.telephone;
    }


    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    /*
    public Set<Pet> getPets() {
        return pets;
    }

    public void setPets(Set<Pet> pets) {
        this.pets = pets;
    }
    */
    @Override
    public String toString() {
        return super.getId().toString() + ',' + super.getFirstName() + ',' +super.getLastName()  + ',' +  this.getAddress() +',' + this.getCity() + ',' + this.getTelephone() + ',' + this.getEmail() ;
    }
}
