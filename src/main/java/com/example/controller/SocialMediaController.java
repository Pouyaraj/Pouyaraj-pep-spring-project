package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService; // Add MessageService

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService; // Initialize MessageService
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        // Check for blank username or password less than 4 characters
        if (account.getUsername() == null || account.getUsername().trim().isEmpty() || 
            account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password.");
        }

        try {
            Account savedAccount = accountService.register(account);
            return ResponseEntity.ok(savedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        if (account.getUsername() == null || account.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized.");
        }

        try {
            Account verifiedAccount = accountService.login(account);
            return ResponseEntity.ok(verifiedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid username or password.");
        }
    }

    @PostMapping("/messages") // New endpoint for creating messages
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty() || 
            message.getMessageText().length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid message text.");
        }

        try {
            Message savedMessage = messageService.createMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }
}
