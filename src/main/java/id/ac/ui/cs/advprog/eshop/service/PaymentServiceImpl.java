package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PaymentServiceImpl implements PaymentService {

	// Refactor 1: Extract magic numbers and Strings to constants -Aldo
	private static final String VOUCHER_PREFIX = "ESHOP";
	private static final int VOUCHER_LENGTH = 16;
	private static final int VOUCHER_NUM_DIGITS = 8;
	private static final String PAYMENT_METHOD_VOUCHER = "Voucher";
	private static final String PAYMENT_METHOD_BANK_TRANSFER = "Bank Transfer";

	// Refactor 2: Use constructor injection instead of field injection -Aldo
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	@Autowired
	public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
	}

	@Override
	public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
		Payment payment = new Payment(order.getId(), method, paymentData);

		// Refactor all the corresponding conditions to use the newly defined constants -Aldo
		if (PAYMENT_METHOD_VOUCHER.equals(method)) {
			String voucherCode = paymentData.get("voucherCode");
			if (isValidVoucherCode(voucherCode)) {
				return setStatus(payment, PaymentStatus.SUCCESS.getValue());
			}
			return setStatus(payment, PaymentStatus.REJECTED.getValue());
		}

		if (PAYMENT_METHOD_BANK_TRANSFER.equals(method)) {
			if (isValidBankTransferData(paymentData)) {
				return setStatus(payment, PaymentStatus.SUCCESS.getValue());
			}
			return setStatus(payment, PaymentStatus.REJECTED.getValue());
		}

		paymentRepository.save(payment);
		return payment;
	}

	@Override
	public Payment setStatus(Payment payment, String status) {
		payment.setStatus(status);

		Order order = orderRepository.findById(payment.getId());
		if (order == null) {
			throw new NoSuchElementException();
		}

		if (PaymentStatus.SUCCESS.getValue().equals(status)) {
			order.setStatus(OrderStatus.SUCCESS.getValue());
		} else {
			order.setStatus(OrderStatus.FAILED.getValue());
		}

		orderRepository.save(order);
		paymentRepository.save(payment);
		return payment;
	}

	@Override
	public Payment getPayment(String paymentId) {
		return paymentRepository.findById(paymentId);
	}

	@Override
	public List<Payment> getAllPayment() {
		return paymentRepository.findAll();
	}

	// Refactor 3: Removed redundant getAllPayments() method here
	public boolean isValidVoucherCode(String voucherCode) {
		// Refactor conditions to use the newly defined constants as well  -Aldo
		if (voucherCode == null || voucherCode.length() != VOUCHER_LENGTH) {
			return false;
		}

		if (!voucherCode.startsWith(VOUCHER_PREFIX)) {
			return false;
		}

		long digitCount = voucherCode.chars()
				.filter(Character::isDigit)
				.count();
		return digitCount == VOUCHER_NUM_DIGITS;
	}

	public boolean isValidBankTransferData(Map<String, String> paymentData) {
		String bankName = paymentData.get("bankName");
		String referenceCode = paymentData.get("referenceCode");

		return bankName != null
				&& !bankName.isBlank()
				&& referenceCode != null
				&& !referenceCode.isBlank();
	}
}