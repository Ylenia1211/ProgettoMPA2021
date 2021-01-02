package model;


import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

public class Owner extends Person {

    private int tot_visit;

    public Owner(String name, String surname, Gender sex, LocalDate datebirth, String address, String city, String telephone, String email) {
        super(name, surname, sex, datebirth, address, city, telephone, email);
        tot_visit = 0;
    }


    public Owner() {
        super();
    }

    public int getTot_visit() {
        return tot_visit;
    }

    public void setTot_visit(int tot_visit) {
        this.tot_visit = tot_visit;
    }
}
