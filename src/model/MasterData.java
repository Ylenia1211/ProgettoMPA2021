package model;

import java.time.LocalDate;
import java.util.UUID;

public abstract class MasterData {

    public static abstract class Builder<T extends Builder<T>>{
        protected String id;
        protected String name;
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
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public Gender getSex() {
        return this.sex;
    }

    public LocalDate getDatebirth() {
        return this.datebirth;
    }

    @Override
    public String toString() {
        return "MasterData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", sex=" + sex +
                ", datebirth=" + datebirth +
                '}';
    }
}
