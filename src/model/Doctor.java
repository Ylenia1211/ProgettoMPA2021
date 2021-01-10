package model;

public class Doctor extends Person{

    public static class Builder<T extends Doctor.Builder<T>>  extends Person.Builder<Doctor.Builder<T>>{

        protected String specialitation;
        protected String username;
        protected String password;

        public Builder(String specialitation, String username, String password){
            super();
            this.specialitation = specialitation;
            this.username = username;
            this.password = password;
        }

        @Override
        public Doctor.Builder getThis(){
            return  this;
        }

        public Doctor build(){
            return new Doctor(this);
        }
    }

    final private String specialitation;
    final private String username;
    final private String password;

    protected Doctor(Doctor.Builder builder){
        super (builder);
        this.specialitation = builder.specialitation;
        this.username = builder.username;
        this.password = builder.password;
    }


    public String getSpecialitation() {
        return specialitation;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

