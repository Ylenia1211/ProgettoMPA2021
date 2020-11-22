package model;


public class Person {

    private final Integer id;
    public static int counter = 0;

    private String firstName;
    private String lastName;

    public Person() {
        this.id = ++counter;
    }

    public Integer getId() {
        return id;
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
}

