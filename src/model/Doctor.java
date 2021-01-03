package model;

import javax.print.Doc;

public class Doctor extends Person{

    public static class Builder<T extends Doctor.Builder<T>>  extends Person.Builder<Doctor.Builder<T>>{

        protected String id_specialitation;
        protected String username;
        protected String password;

        public Builder(){
            super();
        }

        @Override
        public Doctor.Builder getThis(){
            return  this;
        }

        public Doctor build(){
            return new Doctor(this);
        }
    }

    final private String id_specialitation;
    final private String username;
    final private String password;

    protected Doctor(Doctor.Builder builder){
        super (builder);
        this.id_specialitation = builder.id_specialitation;
        this.username = builder.username;
        this.password = builder.password;
    }

    public String getId_specialitation() {
        return id_specialitation;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

