package com.enviro.assessment.grad001.lutendodamuleli.controller;

import com.enviro.assessment.grad001.lutendodamuleli.entity.Investor;
import com.enviro.assessment.grad001.lutendodamuleli.entity.Product;
import com.enviro.assessment.grad001.lutendodamuleli.entity.WithdrawalRequest;
import com.enviro.assessment.grad001.lutendodamuleli.model.MailStructure;
import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import com.enviro.assessment.grad001.lutendodamuleli.model.View;
import com.enviro.assessment.grad001.lutendodamuleli.service.EmailSenderService;
import com.enviro.assessment.grad001.lutendodamuleli.service.InvestorService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investment")
@RequiredArgsConstructor
public class InvestorController {

    private final InvestorService investorService;
    private final EmailSenderService emailSenderService;

    @Operation(summary = "retrieve all investor records in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No investors inside the database")
    })
    @GetMapping(value = "/investors", produces = "application/json")
    @JsonView(value = {View.Base.class})
    public ResponseEntity<List<Investor>> getAllInvestors(){
        return ResponseEntity.ok(
                investorService.retrieveAllInvestors()
        );
    }

    @Operation(summary = "retrieve a specific investor record in the db by investorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request"),
            @ApiResponse(responseCode = "404", description = "Investor does not exist, check the id")

    })
    @GetMapping(value = "/investors/{investorId}", produces = "application/json")
    @JsonView(value = {View.Base.class})
    public ResponseEntity<Investor> createInvestor(@PathVariable Long investorId){
        return ResponseEntity.ok(investorService.retrieveInvestorById(investorId));
    }

    @Operation(summary = "create investor record in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request")
    })
    @PostMapping(value = "/investors", produces = "application/json")
    public void createInvestor(@RequestBody Investor investor){
        investorService.saveInvestor(investor);
    }

    @Operation(summary = "updating or adding product to the investor by investorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request"),
            @ApiResponse(responseCode = "404", description = "Investor does not exist, check the id")

    })
    @PutMapping(value = "/investors/{investorId}/product", produces = "application/json")
    public void updateProduct(@PathVariable Long investorId, @RequestBody Product product){
        investorService.updateInvestorProduct(investorId, product);
    }

    @Operation(summary = "request a withdrawal using an investorId & productType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request"),
            @ApiResponse(responseCode = "404", description = "Investor does not exist, check the id")

    })
    @PostMapping(value = "/investors/{investorId}/withdraw/{productType}", produces = "application/json")
    public ResponseEntity<?> withdrawal(@PathVariable Long investorId,
                           @PathVariable ProductType productType,
                           @RequestBody WithdrawalRequest request){
        return investorService.withdraw(investorId, productType, request);
    }

    @Operation(summary = "request all withdrawal belonging to the investor & productType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request"),
            @ApiResponse(responseCode = "404", description = "Investor does not exist, check the id")

    })
    @GetMapping(value = "/investors/{investorId}/statement/{productType}", produces = "application/json")
    public List<WithdrawalRequest> getAllWithdrawalsById(@PathVariable Long investorId,
                                                         @PathVariable ProductType productType){
        return investorService.retrieveWithdrawalsById(investorId, productType);
    }

    @Operation(summary = "link to download a statement for a given investorId & productType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request"),
            @ApiResponse(responseCode = "404", description = "Investor does not exist, check the id")

    })
    @GetMapping(value = "/investors/{investorId}/statement/{productType}/download", produces = "application/json")
    public void downloadStatement(@PathVariable Long investorId,
                           @PathVariable ProductType productType, HttpServletResponse response) throws Exception{
        investorService.getAllWithdrawalsById(investorId, productType, response);
    }
}
