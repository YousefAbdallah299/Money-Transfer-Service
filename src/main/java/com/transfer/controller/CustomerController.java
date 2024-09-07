package com.transfer.controller;

import com.transfer.dto.request.FavoritesRequestDTO;
import com.transfer.dto.response.AccountResponseDTO;
import com.transfer.dto.response.FavoritesResponseDTO;
import com.transfer.exception.custom.ResourceNotFoundException;
import com.transfer.service.CustomerService;
import com.transfer.service.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@Validated
@Tag(name = "Customers", description = "Endpoints for managing customers and their transactions")
public class CustomerController {

    private final CustomerService customerService;
    private final JwtUtils jwtUtils;

    @GetMapping("/accounts")
    @Operation(summary = "Get customer's accounts", description = "Retrieves the list of accounts for the logged-in customer.")
    public ResponseEntity<List<AccountResponseDTO>> getAccounts(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        List<AccountResponseDTO> accounts = customerService.getAccounts(loggedInUserEmail);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }


    @GetMapping("/favorites")
    @Operation(summary = "Get favorite recipients", description = "Retrieves the list of favorite recipients for the logged-in customer.")
    public ResponseEntity<Set<FavoritesResponseDTO>> getFavorites(@RequestHeader("Authorization") String token) throws ResourceNotFoundException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        Set<FavoritesResponseDTO> favorites = customerService.getFavorites(loggedInUserEmail);
        return new ResponseEntity<>(favorites, HttpStatus.OK);
    }

    @PostMapping("/favorites")
    @Operation(summary = "Add a favorite recipient", description = "Adds a recipient to the logged-in customer's favorites.")
    public ResponseEntity<Void> addFavorite(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid FavoritesRequestDTO favoritesRequestDTO
    ) throws ResourceNotFoundException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        customerService.addFavorite(favoritesRequestDTO, loggedInUserEmail);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/delete")
    @Operation(summary = "Delete a favorite recipient", description = "Removes a recipient from the logged-in customer's favorites.")
    public ResponseEntity<Void> deleteFavorite(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid FavoritesRequestDTO favoritesRequestDTO
    ) throws ResourceNotFoundException {
        token = token.substring(7);
        String loggedInUserEmail = jwtUtils.getEmailFromJwtToken(token);
        customerService.deleteFavorite(favoritesRequestDTO, loggedInUserEmail);
        return ResponseEntity.ok().build();
    }
}
