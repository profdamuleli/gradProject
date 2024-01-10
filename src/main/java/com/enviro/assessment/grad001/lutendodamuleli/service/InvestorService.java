package com.enviro.assessment.grad001.lutendodamuleli.service;

import com.enviro.assessment.grad001.lutendodamuleli.entity.Investor;
import com.enviro.assessment.grad001.lutendodamuleli.entity.Product;
import com.enviro.assessment.grad001.lutendodamuleli.entity.WithdrawalRequest;
import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InvestorService {

    Investor retrieveInvestorById(Long investorId);

    void updateInvestorProduct(Long investorId, Product product);

    void saveInvestor(Investor investor);

    List<Product> retrieveProductsById(Long investorId);

    ResponseEntity<?> withdraw(Long investorId,
                                   ProductType productType,
                                   WithdrawalRequest request);

    List<WithdrawalRequest> retrieveWithdrawalsById(Long investorId,
                                                           ProductType productType);

    List<Investor> retrieveAllInvestors();

    void getAllWithdrawalsById(Long investorId,
                                      ProductType productType,
                                      HttpServletResponse response) throws Exception;

}
