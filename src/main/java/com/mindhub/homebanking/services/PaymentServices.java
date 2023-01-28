package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.PaymentDTO;
import com.mindhub.homebanking.models.Payment;

import java.util.List;

public interface PaymentServices {

    public List<Payment> getPayments();

    public List<PaymentDTO> getPaymentsDTO(List<Payment> payments);
    public Payment getPayment(Long id);
    public void savePayment(Payment payment);

}
