package koster.tychokoster_pset678;

// User class with email and nickname stored.

class User {
    private String email;
    private String nickname;

    User(){
    }

    User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    // Gets email of a user.
    public String getEmail() {
        return email;
    }

    // Gets nickname of a user.
    public String getNickname() {
        return nickname;
    }
}
