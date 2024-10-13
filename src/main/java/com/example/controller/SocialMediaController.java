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
    private final MessageService messageService; 

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty() || 
            account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(400).body("Error.");
        }

        try {
            Account savedAccount = accountService.register(account);
            return ResponseEntity.ok(savedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body("Duplicate username.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        if (account.getUsername() == null || account.getPassword() == null) {
            return ResponseEntity.status(401).body("Unauthorized.");
        }

        try {
            Account verifiedAccount = accountService.login(account);
            return ResponseEntity.ok(verifiedAccount);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body("Error.");
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty() || 
            message.getMessageText().length() > 255) {
            return ResponseEntity.status(400).body("Invalid message text.");
        }

        try {
            Message savedMessage = messageService.createMessage(message);
            return ResponseEntity.ok(savedMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer messageId) {
        int rowsUpdated = messageService.deleteMessage(messageId);
        
        if (rowsUpdated > 0) {
            return ResponseEntity.ok(rowsUpdated);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage( @PathVariable Integer messageId, @RequestBody Message message) {
        
        if (message.getMessageText() == null || 
            message.getMessageText().trim().isEmpty() || 
            message.getMessageText().length() > 255) {
            return ResponseEntity.status(400).body("Invalid message text.");
        }

        try {
            int rowsUpdated = messageService.updateMessage(messageId, message.getMessageText());
            if (rowsUpdated == 1) {
                return ResponseEntity.ok(rowsUpdated);
            } else {
                return ResponseEntity.status(400).body("Message update failed.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return ResponseEntity.ok(messages);
    }

    
}
