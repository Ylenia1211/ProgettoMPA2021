package model;

public class User {

    private final String username;
    private final String password;
    private final String role;

    public User(Builder builder) {
        username = builder.username;
        password = builder.password;
        role = builder.role;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public static class Builder{
        private  String username;
        private  String password;
        private  String role;

        public Builder() {
        }

        public  Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public  Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public  Builder setRole(String role) {
            this.role = role;
            return this;
        }
        public User build(){
            return new User(this);
        }
    }

}
