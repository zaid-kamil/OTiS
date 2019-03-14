package digipodium.otis;

public class PayTransaction {

    public String senderName;
    public String amt;
    public String bank;
    public String ifsc;
    public String account;
    public String uid;

    public PayTransaction() {
    }

    public PayTransaction(String senderName, String amt, String bank, String ifsc, String account, String uid) {
        this.senderName = senderName;
        this.amt = amt;
        this.bank = bank;
        this.ifsc = ifsc;
        this.account = account;
        this.uid = uid;
    }
}
