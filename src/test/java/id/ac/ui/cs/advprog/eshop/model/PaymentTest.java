package id.ac.ui.cs.advprog.eshop.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Payment payment;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "PROMOHEMAT");
        this.paymentData.put("voucherDiscount", "50000");

        this.payment = new Payment("payment-1", "Voucher", "SUCCESS", this.paymentData);
    }

    @Test
    void testCreatePayment() {
        assertEquals("payment-1", this.payment.getId());
        assertEquals("Voucher", this.payment.getMethod());
        assertEquals("SUCCESS", this.payment.getStatus());
        assertSame(this.paymentData, this.payment.getPaymentData());
        assertEquals("PROMOHEMAT", this.payment.getPaymentData().get("voucherCode"));
        assertEquals("50000", this.payment.getPaymentData().get("voucherDiscount"));
    }

    @Test
    void testSetPaymentId() {
        this.payment.setId("payment-2");

        assertEquals("payment-2", this.payment.getId());
    }

    @Test
    void testSetPaymentMethod() {
        this.payment.setMethod("Transfer");

        assertEquals("Transfer", this.payment.getMethod());
    }

    @Test
    void testSetPaymentStatus() {
        this.payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", this.payment.getStatus());
    }

    @Test
    void testSetPaymentData() {
        Map<String, String> newPaymentData = new HashMap<>();
        newPaymentData.put("bankName", "BCA");
        newPaymentData.put("accountNumber", "1234567890");

        this.payment.setPaymentData(newPaymentData);

        assertSame(newPaymentData, this.payment.getPaymentData());
        assertEquals("BCA", this.payment.getPaymentData().get("bankName"));
        assertEquals("1234567890", this.payment.getPaymentData().get("accountNumber"));
    }

    @Test
    void testSetInvalidPaymentStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment invalidPayment = new Payment("payment-3", "Voucher", "InvalidStatus", this.paymentData);
        });

    }
}