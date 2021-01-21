package model;

public class Secretariat extends Person {

    public static class Builder<T extends Secretariat>  extends Person.Builder<Secretariat.Builder<T>>{


        protected String username;
        protected String password;

        public Builder(){
            super();
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
        public Secretariat.Builder getThis(){
            return  this;
        }

        public Secretariat build(){
            return new Secretariat(this);
        }
    }


    final private String username;
    final private String password;

    protected Secretariat(Builder builder){
        super (builder);
        this.username = builder.username;
        this.password = builder.password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

}
