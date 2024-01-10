package com.enviro.assessment.grad001.lutendodamuleli.service.impl;

import com.enviro.assessment.grad001.lutendodamuleli.entity.Investor;
import com.enviro.assessment.grad001.lutendodamuleli.entity.Product;
import com.enviro.assessment.grad001.lutendodamuleli.entity.WithdrawalRequest;
import com.enviro.assessment.grad001.lutendodamuleli.exception.InvestorNotFoundException;
import com.enviro.assessment.grad001.lutendodamuleli.model.MailStructure;
import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import com.enviro.assessment.grad001.lutendodamuleli.model.WithdrawalNotice;
import com.enviro.assessment.grad001.lutendodamuleli.repository.InvestorRepository;
import com.enviro.assessment.grad001.lutendodamuleli.repository.WithdrawalRepository;
import com.enviro.assessment.grad001.lutendodamuleli.service.EmailSenderService;
import com.enviro.assessment.grad001.lutendodamuleli.service.InvestorService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
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
    public void saveInvestor(Investor investor) {
        investorRepository.save(investor);

    }

    @Override
    public ResponseEntity<?> withdraw(Long investorId, ProductType productType, WithdrawalRequest request) {
        Investor investor = null;

        try{
            if(investorRepository.existsById(investorId)){
                investor = retrieveInvestorById(investorId);
                List<Product> products = retrieveInvestorById(investorId).getProducts();
                //Check is the product exist within the product linked
                if(!products.isEmpty()) {
                    for (Product product : products) {
                        if (product.getType().equals(productType)) {
                            if (product.getType().equals(ProductType.RETIREMENT)) {
                                if ((calculateAge(investor.getPersonal().getBirth_date(), LocalDate.now()))
                                        && request.getWithdrawalAmount() < product.getCurrentBalance()
                                        && (allowedPercentageToWithdraw(request, product))) {
                                    //TODO: create a withdrawal notification for retirement and the status should be in-progress

                                    request.setProductType(ProductType.RETIREMENT);
                                    request.setWithdrawalDate(LocalDate.now());
                                    investor.getWithdrawalRequest().add(request);

                                    WithdrawalNotice withdrawalNotice = withdrawal_notice(product, request);

                                    MailStructure mailStructure = new MailStructure();
                                    mailStructure.setSubject("Testing");

                                    mailStructure.setMessage("*Withdrawal notification*\n\n"
                                            + "Current balance : " + withdrawalNotice.getCurrentBalance() + "\n"
                                            + "Amount withdrawn : " + withdrawalNotice.getAmountWithdrawn() + "\n"
                                            + "Paid To : " + request.getAccountNumber() + "\n"
                                            + "Closing balance : " + withdrawalNotice.getClosingBalance());

                                    emailSenderService.sendEmail("lutendo.damuleli.f@gmail.com", mailStructure);

                                    investor.getProducts().get(product.getProductId().intValue() - 1)
                                            .setCurrentBalance(withdrawalNotice.getClosingBalance());
                                    investor.getProducts().get(product.getProductId().intValue() - 1)
                                            .setPreviousBalance(withdrawalNotice.getCurrentBalance());
                                    request.setClosingAmount(withdrawalNotice.getClosingBalance());


                                    investorRepository.save(investor);

                                    return null;
                                }
                            } else {
                                //TODO: create a withdrawal notification for savings in-progress
                                request.setProductType(ProductType.SAVINGS);
                                request.setWithdrawalDate(LocalDate.now());
                                investor.getWithdrawalRequest().add(request);

                                WithdrawalNotice withdrawalNotice = withdrawal_notice(product, request);

                                MailStructure mailStructure = new MailStructure();
                                mailStructure.setSubject("Testing");

                                mailStructure.setMessage("*Withdrawal notification*\n\n"
                                        + "Current balance : " + withdrawalNotice.getCurrentBalance() + "\n"
                                        + "Amount withdrawn : " + withdrawalNotice.getAmountWithdrawn() + "\n"
                                        + "Paid To : " + request.getAccountNumber() + "\n"
                                        + "Closing balance : " + withdrawalNotice.getClosingBalance());

                                emailSenderService.sendEmail("lutendo.damuleli.f@gmail.com", mailStructure);

                                investor.getProducts().get(product.getProductId().intValue() - 1)
                                        .setCurrentBalance(withdrawalNotice.getClosingBalance());
                                investor.getProducts().get(product.getProductId().intValue() - 1)
                                        .setPreviousBalance(withdrawalNotice.getCurrentBalance());
                                request.setClosingAmount(withdrawalNotice.getClosingBalance());


                                investorRepository.save(investor);

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
    public List<WithdrawalRequest> retrieveWithdrawalsById(Long investorId,
                                                           ProductType productType){
        Investor investor = retrieveInvestorById(investorId);
        return investor.getWithdrawalRequest();
    }

    @Override
    public List<Investor> retrieveAllInvestors() {
        List<Investor> investorList = investorRepository.findAll();
        if(investorList.isEmpty())
            throw new InvestorNotFoundException("No investors available");
        return investorList;    }

    @Override
    public void getAllWithdrawalsById(Long investorId,ProductType productType, HttpServletResponse response)
            throws Exception {
        //set file name and content type
        String filename = "withdrawal-statement.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<WithdrawalRequest> writer = new StatefulBeanToCsvBuilder<WithdrawalRequest>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();



        List<WithdrawalRequest> withdrawalRequestList = new ArrayList<>();
        if (investorRepository.existsById(investorId)) {
            Investor investor = retrieveInvestorById(investorId);
            List<WithdrawalRequest> requestList = investor.getWithdrawalRequest();

            if(productType.equals(ProductType.RETIREMENT)){
                for(WithdrawalRequest request : requestList){
                    if(request.getProductType().equals(ProductType.RETIREMENT))
                        withdrawalRequestList.add(request);
                }

            } else if(productType.equals(ProductType.SAVINGS)){
                    for(WithdrawalRequest request : requestList){
                        if(request.getProductType().equals(ProductType.SAVINGS))
                            withdrawalRequestList.add(request);
                    }
            }
            writer.write(withdrawalRequestList.listIterator());
        }
        //Write all withdrawals data to csv file
        //return withdrawalRequestList;
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



/*


    public void *//*List<WithdrawalRequest>*//* getAllWithdrawalsById(Long investorId,
                                                                  ProductType productType,
                                                                  HttpServletResponse response) throws Exception {
        //set file name and content type
        String filename = "withdrawal-statement.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<WithdrawalRequest> writer = new StatefulBeanToCsvBuilder<WithdrawalRequest>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();



        List<WithdrawalRequest> withdrawalRequestList = null;
        if (investorRepository.existsById(investorId)) {
            Investor investor = retrieveInvestorById(investorId);
            withdrawalRequestList = investor.getWithdrawalRequest();
            writer.write(withdrawalRequestList.listIterator());
        }
        //Write all withdrawals data to csv file
        //return withdrawalRequestList;
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
    }*/
}
