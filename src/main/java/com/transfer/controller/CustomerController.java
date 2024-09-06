package com.transfer.controller;


import com.transfer.dto.AddFavoritesDTO;
import com.transfer.dto.CustomerDTO;
import com.transfer.dto.ReturnFavoritesDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.service.CustomerService;
import com.transfer.service.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    private final JwtUtils jwtUtils;

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable Long customerId) throws ResourceNotFoundException {
        return this.customerService.getCustomerById(customerId);
    }

    @PutMapping("/transfer/{senderID}/{receiverAccountId}/{amount}")
    public ResponseEntity<Void> transfer(@RequestHeader("Authorization") String token,@PathVariable(value = "senderID") Long senderID,@PathVariable(value = "receiverAccountId") Long receiverID, @PathVariable(value = "amount") Double amount) throws ResourceNotFoundException{
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        this.customerService.transfer(senderID,receiverID, amount,loggedInUserEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<Set<ReturnFavoritesDTO>> getFavorites(@RequestHeader("Authorization") String token) throws ResourceNotFoundException{
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        return new ResponseEntity<>(customerService.getFavorites(loggedInUserEmail), HttpStatus.OK);
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> addFavorite(@RequestHeader("Authorization") String token,@RequestBody @Valid AddFavoritesDTO addFavoritesDTO) throws ResourceNotFoundException{

        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        customerService.addFavorite(addFavoritesDTO,loggedInUserEmail);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("favorites/delete")
    public ResponseEntity<Void> deleteFavorite(@RequestHeader("Authorization") String token,@RequestBody @Valid AddFavoritesDTO addFavoritesDTO) throws ResourceNotFoundException{

        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        customerService.deleteFavorite(addFavoritesDTO,loggedInUserEmail);

        return ResponseEntity.ok().build();
    }
}