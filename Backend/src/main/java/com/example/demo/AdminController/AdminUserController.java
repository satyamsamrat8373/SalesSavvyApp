package com.example.demo.AdminController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.AdminService.AdminUserService;
import com.example.demo.entities.Users;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(
    origins = "http://localhost:5173",
    allowCredentials = "true",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
public class AdminUserController {
    private AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modifyUser(@RequestBody Map<String, Object> userRequest) {
        try {
            // Parse userId safely
            Integer userId = null;
            if (userRequest.get("userId") instanceof String) {
                userId = Integer.parseInt((String) userRequest.get("userId"));
            } else if (userRequest.get("userId") instanceof Number) {
                userId = ((Number) userRequest.get("userId")).intValue();
            } else {
                throw new IllegalArgumentException("Invalid userId format");
            }

            String username = (String) userRequest.get("username");
            String email = (String) userRequest.get("email");
            String role = (String) userRequest.get("role");

            if (username == null || email == null || role == null) {
                throw new IllegalArgumentException("Username, email, and role are required");
            }

            Users updatedUser = adminUserService.modifyUser(userId, username, email, role);
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", updatedUser.getUser_id());
            response.put("username", updatedUser.getUsername());
            response.put("email", updatedUser.getEmail());
            response.put("role", updatedUser.getRole().name());
            response.put("createdAt", updatedUser.getCreated_at());
            response.put("updatedAt", updatedUser.getUpdated_at());
            response.put("message", "User modified successfully");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "status", "error"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Something went wrong",
                "status", "error"
            ));
        }
    }

    @GetMapping("/getbyid")
    public ResponseEntity<?> getUserById(@RequestParam Integer userId) {
        try {
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "User ID is required",
                    "status", "error"
                ));
            }

            Users user = adminUserService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "error", "User not found",
                    "status", "error"
                ));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUser_id());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("role", user.getRole().name());
            response.put("createdAt", user.getCreated_at());
            response.put("updatedAt", user.getUpdated_at());
            response.put("status", "success");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "status", "error"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Something went wrong",
                "status", "error"
            ));
        }
    }

    // Add this method to handle POST requests for user details
    @PostMapping("/getbyid")
    public ResponseEntity<?> getUserByIdPost(@RequestBody Map<String, Object> request) {
        try {
            Integer userId = null;
            if (request.get("userId") instanceof String) {
                userId = Integer.parseInt((String) request.get("userId"));
            } else if (request.get("userId") instanceof Number) {
                userId = ((Number) request.get("userId")).intValue();
            } else {
                throw new IllegalArgumentException("Invalid userId format");
            }
            
            return getUserById(userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "status", "error"
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Something went wrong",
                "status", "error"
            ));
        }
    }
}