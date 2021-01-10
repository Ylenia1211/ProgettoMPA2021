package model;


import java.time.LocalDate;

public class Owner extends Person {

    public static class Builder<T extends Builder<T>>  extends Person.Builder<Builder<T>>{
        protected int tot_animal;

        public Builder(){
            super();
            this.tot_animal = 0;
        }

        @Override
        public Builder getThis(){
            return  this;
        }

        public Owner build(){
            return new Owner(this);
        }
    }


    final private int tot_animal;

    protected Owner(Builder builder){
        super (builder);
        this.tot_animal = builder.tot_animal;
    }

    public int getTot_visit() {
        return tot_animal;
    }
    /*
    public Owner(String name, String surname, Gender sex, LocalDate datebirth, String address, String city, String telephone, String email) {
        super(name, surname, sex, datebirth, address, city, telephone, email);
        tot_visit = 0;
    }
     */
}
