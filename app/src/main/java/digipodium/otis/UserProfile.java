package digipodium.otis;

class UserProfile {

    public String name;
    public String ifsc;
    public String bank;
    public String account;
    public String uid;
    public String email;
    public String phoneNumber;

    public UserProfile() {
    }

    public UserProfile(String name, String ifsc, String bank, String account, String uid, String email, String phoneNumber) {

        this.name = name;
        this.ifsc = ifsc;
        this.bank = bank;
        this.account = account;
        this.uid = uid;
        this.email = email;
        this.phoneNumber = phoneNumber;

    }
}
