package com.shunshousong.controller;

import com.shunshousong.dto.UserDTOs.*;
import com.shunshousong.entity.User;
import com.shunshousong.service.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findOne(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody CreateUserDto dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @GetMapping("/check-phone/{phone}")
    public ResponseEntity<Map<String, Boolean>> checkPhone(@PathVariable String phone) {
        User user = userService.findByPhone(phone);
        Map<String, Boolean> result = new HashMap<>();
        result.put("available", user == null);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<User> addDeposit(@PathVariable Long id, @RequestBody Map<String, Double> body) {
        return ResponseEntity.ok(userService.addDeposit(id, body.get("amount")));
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<User> updateRating(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(userService.updateRating(id, body.get("rating")));
    }
}
