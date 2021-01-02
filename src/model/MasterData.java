package model;

import java.time.LocalDate;

public class MasterData {
    private Long id;
    private static long counter = 0;
    private String name;
    private String surname;
    private Gender sex;
    private LocalDate datebirth;

    public MasterData() {
        id = counter+1;
    }

    public MasterData(String name, String surname, Gender sex, LocalDate datebirth) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.datebirth = datebirth;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSex() {
        return sex.toString();
    }

    public void setSex(Gender sex) {
        this.sex = sex;
    }

    public LocalDate getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(LocalDate datebirth) {
        this.datebirth = datebirth;
    }
}
