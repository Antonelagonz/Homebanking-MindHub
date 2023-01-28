package com.mindhub.homebanking.services.implementation;

import com.mindhub.homebanking.dto.PaymentDTO;
import com.mindhub.homebanking.models.Payment;
import com.mindhub.homebanking.repositories.PaymentRepository;
import com.mindhub.homebanking.services.PaymentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServicesImplementation implements PaymentServices {

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public List<Payment> getPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments;
    }

    @Override
    public List<PaymentDTO> getPaymentsDTO(List<Payment> payments) {
        return payments.stream()
                .map(PaymentDTO::new)
                .collect(Collectors.toList());

    }

    @Override
    public Payment getPayment(Long id) {
        Payment payment = paymentRepository.findById(id).get();
        return payment;
    }

    @Override
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
