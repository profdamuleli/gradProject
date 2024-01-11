package com.enviro.assessment.grad001.lutendodamuleli.service.impl;

import com.enviro.assessment.grad001.lutendodamuleli.entity.Investor;
import com.enviro.assessment.grad001.lutendodamuleli.entity.Product;
import com.enviro.assessment.grad001.lutendodamuleli.entity.Withdrawal;
import com.enviro.assessment.grad001.lutendodamuleli.entity.WithdrawalRequest;
import com.enviro.assessment.grad001.lutendodamuleli.exception.InvestorNotFoundException;
import com.enviro.assessment.grad001.lutendodamuleli.model.MailStructure;
import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import com.enviro.assessment.grad001.lutendodamuleli.model.WithdrawalNotice;
import com.enviro.assessment.grad001.lutendodamuleli.repository.InvestorRepository;
import com.enviro.assessment.grad001.lutendodamuleli.repository.WithdrawalRepository;
import com.enviro.assessment.grad001.lutendodamuleli.service.EmailSenderService;
import com.enviro.assessment.grad001.lutendodamuleli.service.InvestorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@AllArgsConstructor
public class InvestorServiceImpl implements InvestorService {

    private final InvestorRepository investorRepository;
    private final WithdrawalRepository withdrawalRepository;
    private final EmailSenderService emailSenderService;


    @Override
    public Investor retrieveInvestorById(Long investorId) {
        return investorRepository.findById(investorId)
                .orElseThrow(() -> new InvestorNotFoundException("Investor with ID: "
                        + investorId + " does not exist"));    }

    @Override
    public void updateInvestorProduct(Long investorId, Product product) {
        if(investorRepository.existsById(investorId)){
            Investor investor = retrieveInvestorById(investorId);
            investor.getProducts().add(product);
            saveInvestor(investor);
        }
    }

    @Override
    public List<Product> retrieveProductsById(Long investorId){
        Investor investor = investorRepository.findById(investorId)
                .orElseThrow(() -> new InvestorNotFoundException("Investor with ID: "
                        + investorId + " does not exist"));
        return investor.getProducts();
    }

    @Override
    public void saveInvestor(Investor investor) {
        investorRepository.save(investor);
    }

    @Override
    public ResponseEntity<?> withdraw(Long investorId, ProductType productType, WithdrawalRequest request) {
        Investor investor = null;
        Withdrawal withdrawal = null;

        try{
            if(investorRepository.existsById(investorId)){
                investor = retrieveInvestorById(investorId);
                List<Product> products = investor.getProducts();
                //Check is the product withdrawing from exist within the product linked
                if(!products.isEmpty()) {
                    for (Product product : products) {
                        if (product.getType().name().equals(productType.name())) {
                            if (product.getType().name().equals(ProductType.RETIREMENT.name())) {
                                if ((calculateAge(investor.getBirth_date(), LocalDate.now()))
                                        && request.getWithdrawalAmount() < product.getCurrentBalance()
                                        && (allowedPercentageToWithdraw(request, product))) {

                                    withdrawal.setWithdrawalAmount(request.getWithdrawalAmount());
                                    withdrawal.setWithdrawalDate(LocalDate.now());
                                    withdrawal.setAccountNumber(request.getAccountNumber());
                                    withdrawalRepository.save(withdrawal);

                                    WithdrawalNotice withdrawalNotice = withdrawal_notice(product, request);

                                    MailStructure mailStructure = new MailStructure();
                                    mailStructure.setSubject("Testing");

                                    mailStructure.setMessage("*Withdrawal notification*\n\n"
                                            + "Current balance : " + withdrawalNotice.getCurrentBalance() + "\n"
                                            + "Amount withdrawn : " + withdrawalNotice.getAmountWithdrawn() + "\n"
                                            + "Paid To : " + request.getAccountNumber() + "\n"
                                            + "Closing balance : " + withdrawalNotice.getClosingBalance());

                                    emailSenderService.sendEmail("lutendo.damuleli.f@gmail.com", mailStructure);

/*                                    investor.getProducts().get(product.getProductId().intValue() - 1)
                                            .setCurrentBalance(withdrawalNotice.getClosingBalance());
                                    investor.getProducts().get(product.getProductId().intValue() - 1)
                                            .setPreviousBalance(withdrawalNotice.getCurrentBalance());
                                    request.setClosingAmount(withdrawalNotice.getClosingBalance());


                                    investorRepository.save(investor);*/

                                    return null;
                                }
                            } else {
                                //TODO: create a withdrawal notification for savings in-progress
                                withdrawal.setProductType(ProductType.SAVINGS);
                                withdrawal.setWithdrawalDate(LocalDate.now());

                                WithdrawalNotice withdrawalNotice = withdrawal_notice(product, request);

                                MailStructure mailStructure = new MailStructure();
                                mailStructure.setSubject("Testing");

                                mailStructure.setMessage("*Withdrawal notification*\n\n"
                                        + "Current balance : " + withdrawalNotice.getCurrentBalance() + "\n"
                                        + "Amount withdrawn : " + withdrawalNotice.getAmountWithdrawn() + "\n"
                                        + "Paid To : " + request.getAccountNumber() + "\n"
                                        + "Closing balance : " + withdrawalNotice.getClosingBalance());

                                emailSenderService.sendEmail("lutendo.damuleli.f@gmail.com", mailStructure);

/*                                investor.getProducts().get(product.getProductId().intValue() - 1)
                                        .setCurrentBalance(withdrawalNotice.getClosingBalance());
                                investor.getProducts().get(product.getProductId().intValue() - 1)
                                        .setPreviousBalance(withdrawalNotice.getCurrentBalance());
                                request.setClosingAmount(withdrawalNotice.getClosingBalance());


                                investorRepository.save(investor);*/

                                return null;
                            }
                        }
                    }
                }
            } else {
                throw new InvestorNotFoundException("Not allowed to make withdrawal, Investor doesn't exist");
            }
            return (ResponseEntity<?>) ResponseEntity.ok();
        }catch (ResponseStatusException ex){

        }
        return null;
    }

    public WithdrawalNotice withdrawal_notice(Product product, WithdrawalRequest request ){
        //receive a notification that shows them the balance before the withdrawal was made
        Double currentBalance = product.getCurrentBalance();
        // the amount withdrawn
        Double amountWithdrawn = request.getWithdrawalAmount();
        // the closing balance
        Double closingBalance = currentBalance - amountWithdrawn;

        return new WithdrawalNotice(currentBalance,amountWithdrawn,closingBalance);
    }

    @Override
    public List<Investor> retrieveAllInvestors() {
        List<Investor> investorList = investorRepository.findAll();
        if(investorList.isEmpty())
            throw new InvestorNotFoundException("No investors available");
        return investorList;
    }

    public boolean calculateAge(LocalDate dob, LocalDate current){
        Period diff = Period.between(dob, current);
        if(diff.getYears() > 65)
            return false;
        return true;
    }

    public boolean allowedPercentageToWithdraw(WithdrawalRequest request ,Product product){
        Double percentageAllowed = product.getCurrentBalance() * 0.9;

        if(request.getWithdrawalAmount() < percentageAllowed)
            return true;
        return false;
    }
}
