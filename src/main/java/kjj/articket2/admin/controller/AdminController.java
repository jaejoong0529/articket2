package kjj.articket2.admin.controller;

import kjj.articket2.admin.dto.AdminMemberResponseDto;
import kjj.articket2.admin.dto.AdminProductResponseDto;
import kjj.articket2.admin.dto.AdminTransactionResponseDto;
import kjj.articket2.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    private final AdminService adminService;


    @PreAuthorize("hasRole('ADMIN')") // Spring Security 권한 체크
    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdminDashboard() {
        return ResponseEntity.ok("관리자 대시보드");
    }

    @GetMapping("/members")
    public ResponseEntity<List<AdminMemberResponseDto>> getMembers() {
        return ResponseEntity.ok(adminService.getAllMembers());
    }
    @GetMapping("/products")
    public ResponseEntity<List<AdminProductResponseDto>> getProducts() {
        return ResponseEntity.ok(adminService.getAllProducts());
    }
    @GetMapping("/transactions")
    public ResponseEntity<List<AdminTransactionResponseDto>> getTransactions() {
        return ResponseEntity.ok(adminService.getAllTransactions());
    }
    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        adminService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        adminService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
