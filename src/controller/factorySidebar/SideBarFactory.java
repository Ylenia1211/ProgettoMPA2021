package controller.factorySidebar;

public class SideBarFactory {
    public static SideBarAction createSideBar (String roleUser){
        switch (roleUser){
            case "Dottore" -> {
                return new DoctorSideBar();
            }
            case "Amministratore" -> {
                return new AdminSideBar();
            }
            case "Segreteria" -> {
                return new SecretariatSideBar();
            }
            default -> throw new IllegalStateException("Unexpected value: " + roleUser);
        }
    }
}
// Factory client code deve essere nella Dashbord

