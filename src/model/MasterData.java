package model;

import java.time.LocalDate;
import java.util.UUID;

public abstract class MasterData {

    public static abstract class Builder<T extends Builder<T>>{
        protected  String id;
        protected  String name;
        protected String surname;
        protected Gender sex;
        protected LocalDate datebirth;

        public Builder(){
            this.id = UUID.randomUUID().toString();
        }

        public T addName(String name){
            this.name = name;
            return getThis();
        }
        public T addSurname(String surname){
            this.surname = surname;
            return getThis();
        }
        public T addSex(Gender sex){
            this.sex = sex;
            return getThis();
        }
        public T addDateBirth(LocalDate datebirth){
            this.datebirth = datebirth;
            return getThis();
        }
        public abstract T getThis();
    }

    final private String id;
    final private String name;
    final private String surname;
    final private Gender sex;
    final private LocalDate datebirth;

    protected <T extends Builder<T>> MasterData(Builder<T> builder){
        this.id = builder.id;
        this.name = builder.name;
        this.surname = builder.surname;
        this.sex = builder.sex;
        this.datebirth = builder.datebirth;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSex() {
        return sex.toString();
    }

    public LocalDate getDatebirth() {
        return datebirth;
    }


/* //senza Builder
    public MasterData() {
        id = UUID.randomUUID().toString();
    }

    public MasterData(String name, String surname, Gender sex, LocalDate datebirth) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.datebirth = datebirth;
    }

    public String getId() {
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

    */

}
