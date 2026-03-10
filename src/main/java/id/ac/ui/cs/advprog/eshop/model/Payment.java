package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private java.util.Map<String, String> paymentData;

    public Payment(String id, String method, String status, java.util.Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        if (status.equals("REJECTED") || status.equals("SUCCESS")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setMethod(String method) {
        if (method.equals("Voucher") || method.equals("Transfer")) {
            this.method = method;
        } else {
            throw new IllegalArgumentException();
        }
    }

}
