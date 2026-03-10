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

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
		Payment payment = new Payment(order.getId(), method, paymentData);

		if ("Voucher".equals(method)) {
			String voucherCode = paymentData.get("voucherCode");
			if (isValidVoucherCode(voucherCode)) {
				return setStatus(payment, PaymentStatus.SUCCESS.getValue());
			}
			return setStatus(payment, PaymentStatus.REJECTED.getValue());
		}

		if ("Bank Transfer".equals(method)) {
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

	public List<Payment> getAllPayments() {
		return getAllPayment();
	}

	private boolean isValidVoucherCode(String voucherCode) {
		if (voucherCode == null || voucherCode.length() != 16) {
			return false;
		}

		if (!voucherCode.startsWith("ESHOP")) {
			return false;
		}

		long digitCount = voucherCode.chars()
				.filter(Character::isDigit)
				.count();
		return digitCount == 8;
	}

	private boolean isValidBankTransferData(Map<String, String> paymentData) {
		String bankName = paymentData.get("bankName");
		String referenceCode = paymentData.get("referenceCode");

		return bankName != null
				&& !bankName.isBlank()
				&& referenceCode != null
				&& !referenceCode.isBlank();
	}
}
