package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;
    private Order order;
    private Payment payment;
    private Map<String, String> paymentData;
    private Map<String, String> invalidVoucherPaymentData;
	private Map<String, String> bankTransferPaymentData;
	private Map<String, String> invalidBankTransferPaymentData;

    @BeforeEach
    void setUp() {
	List<Product> products = new ArrayList<>();
	Product product = new Product();
	product.setProductId("product-1");
	product.setProductName("Sampo Cap Bambang");
	product.setProductQuantity(2);
	products.add(product);

	order = new Order(
		"payment-1",
		products,
		1708560000L,
		"William"
	);

	paymentData = new HashMap<>();
	paymentData.put("voucherCode", "ESHOP12345678ABC");
	paymentData.put("voucherDiscount", "50000");

	invalidVoucherPaymentData = new HashMap<>();
	invalidVoucherPaymentData.put("voucherCode", "PROMOHEMAT");
	invalidVoucherPaymentData.put("voucherDiscount", "50000");

	bankTransferPaymentData = new HashMap<>();
	bankTransferPaymentData.put("bankName", "BCA");
	bankTransferPaymentData.put("referenceCode", "TRF-20260310-001");

	invalidBankTransferPaymentData = new HashMap<>();
	invalidBankTransferPaymentData.put("bankName", "");
	invalidBankTransferPaymentData.put("referenceCode", "TRF-20260310-002");

	payment = new Payment(
		order.getId(),
		"Voucher",
		PaymentStatus.REJECTED.getValue(),
		paymentData
	);
    }

    @Test
	void testAddPaymentWithValidVoucherSetsStatusToSuccess() {
	doReturn(order).when(orderRepository).findById(order.getId());
	doAnswer(invocation -> invocation.getArgument(0))
		.when(paymentRepository)
		.save(any(Payment.class));
	doAnswer(invocation -> invocation.getArgument(0))
		.when(orderRepository)
		.save(any(Order.class));

	Payment result = paymentService.addPayment(order, "Voucher", paymentData);

	verify(paymentRepository, times(1)).save(any(Payment.class));
	verify(orderRepository, times(1)).save(order);
	assertEquals(order.getId(), result.getId());
	assertEquals("Voucher", result.getMethod());
	assertSame(paymentData, result.getPaymentData());
	assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
	assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
	void testAddPaymentWithInvalidVoucherSetsStatusToRejected() {
	doReturn(order).when(orderRepository).findById(order.getId());
	doAnswer(invocation -> invocation.getArgument(0))
		.when(paymentRepository)
		.save(any(Payment.class));
	doAnswer(invocation -> invocation.getArgument(0))
		.when(orderRepository)
		.save(any(Order.class));

	Payment result = paymentService.addPayment(order, "Voucher", invalidVoucherPaymentData);

	verify(paymentRepository, times(1)).save(any(Payment.class));
	verify(orderRepository, times(1)).save(order);
	assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
	assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
	void testAddPaymentWithValidBankTransferSetsStatusToSuccess() {
	doReturn(order).when(orderRepository).findById(order.getId());
	doAnswer(invocation -> invocation.getArgument(0))
		.when(paymentRepository)
		.save(any(Payment.class));
	doAnswer(invocation -> invocation.getArgument(0))
		.when(orderRepository)
		.save(any(Order.class));

	Payment result = paymentService.addPayment(order, "Bank Transfer", bankTransferPaymentData);

	verify(paymentRepository, times(1)).save(any(Payment.class));
	verify(orderRepository, times(1)).save(order);
	assertEquals("Bank Transfer", result.getMethod());
	assertSame(bankTransferPaymentData, result.getPaymentData());
	assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
	assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
	}

	@Test
	void testAddPaymentWithInvalidBankTransferSetsStatusToRejected() {
	doReturn(order).when(orderRepository).findById(order.getId());
	doAnswer(invocation -> invocation.getArgument(0))
		.when(paymentRepository)
		.save(any(Payment.class));
	doAnswer(invocation -> invocation.getArgument(0))
		.when(orderRepository)
		.save(any(Order.class));

	Payment result = paymentService.addPayment(order, "Bank Transfer", invalidBankTransferPaymentData);

	verify(paymentRepository, times(1)).save(any(Payment.class));
	verify(orderRepository, times(1)).save(order);
	assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
	assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
	}

	@Test
	void testAddPaymentWithOtherMethodKeepsDefaultRejectedStatus() {
	doAnswer(invocation -> invocation.getArgument(0))
		.when(paymentRepository)
		.save(any(Payment.class));

	Payment result = paymentService.addPayment(order, "Cash on Delivery", paymentData);

	verify(paymentRepository, times(1)).save(any(Payment.class));
	verify(orderRepository, never()).findById(any(String.class));
	assertEquals("Cash on Delivery", result.getMethod());
	assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testSetStatusSuccessAlsoUpdatesOrderStatusToSuccess() {
	doReturn(order).when(orderRepository).findById(payment.getId());
	doAnswer(invocation -> invocation.getArgument(0))
		.when(paymentRepository)
		.save(any(Payment.class));
	doAnswer(invocation -> invocation.getArgument(0))
		.when(orderRepository)
		.save(any(Order.class));

	Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

	assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
	assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
	verify(paymentRepository, times(1)).save(any(Payment.class));
	verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testSetStatusRejectedAlsoUpdatesOrderStatusToFailed() {
	Payment successfulPayment = new Payment(
		payment.getId(),
		payment.getMethod(),
		PaymentStatus.SUCCESS.getValue(),
		payment.getPaymentData()
	);

	doReturn(order).when(orderRepository).findById(successfulPayment.getId());
	doAnswer(invocation -> invocation.getArgument(0))
		.when(paymentRepository)
		.save(any(Payment.class));
	doAnswer(invocation -> invocation.getArgument(0))
		.when(orderRepository)
		.save(any(Order.class));

	Payment result = paymentService.setStatus(successfulPayment, PaymentStatus.REJECTED.getValue());

	assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
	assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
	verify(paymentRepository, times(1)).save(any(Payment.class));
	verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testGetPaymentIfIdFound() {
	doReturn(payment).when(paymentRepository).findById(payment.getId());

	Payment result = paymentService.getPayment(payment.getId());

	assertEquals(payment.getId(), result.getId());
	assertEquals(payment.getMethod(), result.getMethod());
	assertEquals(payment.getStatus(), result.getStatus());
    }

    @Test
    void testGetPaymentIfIdNotFound() {
	doReturn(null).when(paymentRepository).findById("missing-payment");

	Payment result = paymentService.getPayment("missing-payment");

	assertNull(result);
    }

    @Test
    void testGetAllPayments() {
	List<Payment> payments = Arrays.asList(
		payment,
		new Payment("payment-2", "Transfer", PaymentStatus.SUCCESS.getValue(), new HashMap<>())
	);

	doReturn(payments).when(paymentRepository).findAll();

	List<Payment> results = paymentService.getAllPayments();

	assertEquals(2, results.size());
	assertEquals(payment.getId(), results.get(0).getId());
	assertEquals("payment-2", results.get(1).getId());
    }

    @Test
    void testSetStatusWithInvalidOrderIdThrowsException() { 
        
    }
}