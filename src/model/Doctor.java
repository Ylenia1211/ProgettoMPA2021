package model;

public class Doctor extends Person{

    public static class Builder<T extends Doctor.Builder<T>>  extends Person.Builder<Doctor.Builder<T>>{

        protected String specialization;
        protected String username;
        protected String password;
        public Builder(){
            super();
            //this.tot_animal = tot_animal;
        }

        public Builder(String specialization, String username, String password){
            super();
            this.specialization = specialization;
            this.username = username;
            this.password = password;
        }
        public Builder addSpecialization(String specialization){
            this.specialization = specialization;
            return this;
        }
        public Builder addUsername(String username){
            this.username = username;
            return this;
        }
        public Builder addPassword(String password){
            this.password = password;
            return this;
        }

        @Override
        public Doctor.Builder getThis(){
            return  this;
        }

        public Doctor build(){
            return new Doctor(this);
        }
    }

    final private String specialization;
    final private String username;
    final private String password;

    public Doctor(Builder builder){
        super (builder);
        this.specialization = builder.specialization;
        this.username = builder.username;
        this.password = builder.password;
    }


    public String getSpecialization() {
        return specialization;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

