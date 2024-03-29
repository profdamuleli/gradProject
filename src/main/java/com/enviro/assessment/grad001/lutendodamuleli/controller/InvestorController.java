package com.enviro.assessment.grad001.lutendodamuleli.controller;

import com.enviro.assessment.grad001.lutendodamuleli.entity.Investor;
import com.enviro.assessment.grad001.lutendodamuleli.entity.Product;
import com.enviro.assessment.grad001.lutendodamuleli.entity.WithdrawalRequest;
import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import com.enviro.assessment.grad001.lutendodamuleli.service.EmailSenderService;
import com.enviro.assessment.grad001.lutendodamuleli.service.InvestorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "save investor record in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully saved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request")
    })
    @PostMapping(value = "/investors", produces = "application/json")
    public void createInvestor(@RequestBody Investor investor){
        investorService.saveInvestor(investor);
    }

    @Operation(summary = "retrieve a specific investor record in the db by investorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request"),
            @ApiResponse(responseCode = "404", description = "Investor does not exist, check the id")

    })
    @GetMapping(value = "/investors/{investorId}", produces = "application/json")
    public ResponseEntity<Investor> retriveInvestorById(@PathVariable Long investorId){
        return ResponseEntity.ok(investorService.retrieveInvestorById(investorId));
    }

    @Operation(summary = "retrieve all investor records in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "No investors inside the database")
    })
    @GetMapping(value = "/investors", produces = "application/json")
    public ResponseEntity<List<Investor>> getAllInvestors(){
        return ResponseEntity.ok(
                investorService.retrieveAllInvestors()
        );
    }

    @Operation(summary = "updating or adding product to the investor by investorId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request"),
            @ApiResponse(responseCode = "404", description = "Investor does not exist, check the id")

    })
    @PutMapping(value = "/investors/{investorId}/products", produces = "application/json")
    public void updateProduct(@PathVariable Long investorId, @RequestBody Product product){
        investorService.updateInvestorProduct(investorId, product);
    }

    @Operation(summary = "retrieving products linked to the investor by investor_id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Something is wrong with the request"),
            @ApiResponse(responseCode = "404", description = "Investor does not exist, check the id")

    })
    @GetMapping(value = "/investors/{investorId}/products", produces = "application/json")
    public List<Product> retrieveProductLinkedToInvestor(@PathVariable Long investorId){
        return investorService.retrieveProductsById(investorId);
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
        //TODO: check for empty values:
        return investorService.withdraw(investorId, productType, request);
    }
}
