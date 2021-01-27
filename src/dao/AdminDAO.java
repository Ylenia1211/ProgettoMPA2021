package dao;

public interface AdminDAO {
    public String searchEmailClinic();
    public String searchPasswordClinic();
    public void updatePasswordClinic(String psw);
    public void updateEmailClinic(String email);
}
