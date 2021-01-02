package model;


import java.sql.Date;
import java.time.LocalDate;

public class Person extends MasterData{

    String address;
    String city;
    String telephone;
    String email;

    public Person(String name,
                  String surname,
                  Gender sex,
                  LocalDate datebirth,
                  String address,
                  String city,
                  String telephone,
                  String email) {


        super.setName(name);
        super.setSurname(surname);
        super.setSex(sex);
        super.setDatebirth(datebirth);
        this.address = address;
        this.city = city;
        this.telephone = telephone;
        this.email = email;
    }

    public Person() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*
    private String firstName;
    private String lastName;

    public Person() {

    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    */

}

