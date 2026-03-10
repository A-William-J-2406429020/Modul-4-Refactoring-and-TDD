package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PaymentRepositoryTest {

	PaymentRepository paymentRepository;
	List<Payment> payments;

	@BeforeEach
	void setup() {
		paymentRepository = new PaymentRepository();

		Map<String, String> voucherPaymentData = new HashMap<>();
		voucherPaymentData.put("voucherCode", "PROMOHEMAT");
		voucherPaymentData.put("voucherDiscount", "50000");

		Map<String, String> transferPaymentData = new HashMap<>();
		transferPaymentData.put("bankName", "BCA");
		transferPaymentData.put("accountNumber", "1234567890");

		payments = new ArrayList<>();
		payments.add(new Payment(
				"payment-1",
				"Voucher",
				"REJECTED",
				voucherPaymentData
		));
		payments.add(new Payment(
				"payment-2",
				"Transfer",
				"SUCCESS",
				transferPaymentData
		));
	}

	@Test
	void testSaveCreate() {
		Payment payment = payments.get(0);

		Payment result = paymentRepository.save(payment);
		Payment findResult = paymentRepository.findById(payment.getId());

		assertEquals(payment.getId(), result.getId());
		assertEquals(payment.getId(), findResult.getId());
		assertEquals(payment.getMethod(), findResult.getMethod());
		assertEquals(payment.getStatus(), findResult.getStatus());
		assertEquals(payment.getPaymentData(), findResult.getPaymentData());
	}

	@Test
	void testSaveUpdate() {
		Payment payment = payments.get(0);
		paymentRepository.save(payment);

		Map<String, String> updatedPaymentData = new HashMap<>();
		updatedPaymentData.put("voucherCode", "PROMOBARU");
		updatedPaymentData.put("voucherDiscount", "75000");

		Payment updatedPayment = new Payment(
				payment.getId(),
				payment.getMethod(),
				"SUCCESS",
				updatedPaymentData
		);

		Payment result = paymentRepository.save(updatedPayment);
		Payment findResult = paymentRepository.findById(payment.getId());

		assertEquals(payment.getId(), result.getId());
		assertEquals(payment.getId(), findResult.getId());
		assertEquals("SUCCESS", findResult.getStatus());
		assertEquals("PROMOBARU", findResult.getPaymentData().get("voucherCode"));
		assertEquals("75000", findResult.getPaymentData().get("voucherDiscount"));
	}

	@Test
	void testFindByIdIfIdFound() {
		for (Payment payment : payments) {
			paymentRepository.save(payment);
		}

		Payment findResult = paymentRepository.findById(payments.get(1).getId());

		assertEquals(payments.get(1).getId(), findResult.getId());
		assertEquals(payments.get(1).getMethod(), findResult.getMethod());
		assertEquals(payments.get(1).getStatus(), findResult.getStatus());
		assertEquals(payments.get(1).getPaymentData(), findResult.getPaymentData());
	}

	@Test
	void testFindByIdIfIdNotFound() {
		for (Payment payment : payments) {
			paymentRepository.save(payment);
		}

		Payment findResult = paymentRepository.findById("missing-payment");

		assertNull(findResult);
	}
}
