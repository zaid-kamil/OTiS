package digipodium.otis;

import com.google.firebase.auth.FirebaseUser;

class OtisUser {

    public String userId;
    public String aadhar;
    public String phonenumber;
    public String email;

    public OtisUser() {
    }

    public OtisUser(String userId, String aadhar, String username, String phonenumber, String email) {

        this.userId = userId;
        this.aadhar = aadhar;
        this.phonenumber = phonenumber;
        this.email = email;

    }
}
