package model;


import java.time.LocalDate;

public class Owner extends Person {

    public static class Builder<T extends Builder<T>>  extends Person.Builder<Builder<T>>{
        protected int tot_visit;

        public Builder(){
            super();
            this.tot_visit = 0;
        }

        @Override
        public Builder getThis(){
            return  this;
        }

        public Owner build(){
            return new Owner(this);
        }
    }


    final private int tot_visit;

    protected Owner(Builder builder){
        super (builder);
        this.tot_visit = builder.tot_visit;
    }

    public int getTot_visit() {
        return tot_visit;
    }
    /*
    public Owner(String name, String surname, Gender sex, LocalDate datebirth, String address, String city, String telephone, String email) {
        super(name, surname, sex, datebirth, address, city, telephone, email);
        tot_visit = 0;
    }
     */
}
