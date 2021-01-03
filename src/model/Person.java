package model;

import java.time.LocalDate;

public abstract class Person extends MasterData{

    public static abstract class Builder<T extends Builder<T>>
            extends MasterData.Builder<Builder<T>>{

        protected String address;
        protected String city;
        protected String telephone;
        protected String email;

        public T addAddress(String address){
            this.address = address;
            return getThis();
        }

        public T addCity(String city){
            this.city = city;
            return getThis();
        }
        public T addTelephone(String telephone){
            this.telephone = telephone;
            return getThis();
        }
        public T addEmail(String email){
            this.email = email;
            return getThis();
        }

        @Override
        public abstract T getThis();
    }

    final private String address;
    final private String city;
    final private String telephone;
    final private String email;

    protected <T extends Builder<T>> Person(Builder builder) {
        super(builder);
        this.address = builder.address;
        this.city = builder.city;
        this.telephone = builder.telephone;
        this.email = builder.email;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

/* Senza BUILDER
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
  */

    /*
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
   */
}

