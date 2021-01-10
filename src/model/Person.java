package model;

import java.time.LocalDate;

public abstract class Person extends MasterData{

    public static abstract class Builder<T extends Builder<T>>
            extends MasterData.Builder<Builder<T>>{

        protected String address;
        protected String city;
        protected String telephone;
        protected String email;
        protected String fiscalCode;

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
        public T addFiscalCode(String code){
            this.fiscalCode = code;
            return getThis();
        }

        @Override
        public abstract T getThis();
    }

    final private String address;
    final private String city;
    final private String telephone;
    final private String email;
    final private String fiscalCode;

    protected <T extends Builder<T>> Person(Builder builder) {
        super(builder);
        this.address = builder.address;
        this.city = builder.city;
        this.telephone = builder.telephone;
        this.email = builder.email;
        this.fiscalCode = builder.fiscalCode;
    }


    //All getter, and NO setter to provde immutability
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

    public String getFiscalCode() {
        return fiscalCode;
    }
}

