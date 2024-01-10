package com.enviro.assessment.grad001.lutendodamuleli.service;

import com.enviro.assessment.grad001.lutendodamuleli.entity.Investor;
import com.enviro.assessment.grad001.lutendodamuleli.entity.Product;
import com.enviro.assessment.grad001.lutendodamuleli.entity.WithdrawalRequest;
import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface InvestorService {

    public Investor retrieveInvestorById(Long investorId);

    public void updateInvestorProduct(Long investorId, Product product);

    public void saveInvestor(Investor investor);

    public ResponseEntity<?> withdraw(Long investorId,
                                   ProductType productType,
                                   WithdrawalRequest request);

    public List<WithdrawalRequest> retrieveWithdrawalsById(Long investorId,
                                                           ProductType productType);

    public List<Investor> retrieveAllInvestors();

    public void getAllWithdrawalsById(Long investorId,
                                      ProductType productType,
                                      HttpServletResponse response) throws Exception;

}
