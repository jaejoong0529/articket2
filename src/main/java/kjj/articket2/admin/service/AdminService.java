package kjj.articket2.admin.service;

import kjj.articket2.admin.AdminConverter;
import kjj.articket2.admin.dto.AdminMemberResponseDto;
import kjj.articket2.admin.dto.AdminProductResponseDto;
import kjj.articket2.admin.dto.AdminTransactionResponseDto;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.exception.ProductNotFoundException;
import kjj.articket2.product.repository.ProductRepository;
import kjj.articket2.transaction.exception.TransactionNotFoundException;
import kjj.articket2.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AdminService {
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    //회원 정보 관리
    public List<AdminMemberResponseDto> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(AdminConverter::toMemberDto)
                .collect(Collectors.toList());
    }

    //거래 내역 관리
    public List<AdminTransactionResponseDto> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(AdminConverter::toTransactionDto)
                .collect(Collectors.toList());
    }

    public List<AdminProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(AdminConverter::toProductDto)
                .collect(Collectors.toList());
    }

    public void deleteMember(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("해당 회원이 존재하지 않습니다."));
        memberRepository.deleteById(memberId);
    }

    public void deleteProduct(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품이 존재하지 않습니다."));
        productRepository.deleteById(productId);
    }

    public void deleteTransaction(Long transactionId) {
        transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("해당 거래 내역이 존재하지 않습니다."));
        transactionRepository.deleteById(transactionId);
    }

    public List<AdminMemberResponseDto> searchMembers(String keyword) {
        return memberRepository.findByUsernameContainingOrNicknameContainingOrEmailContaining(keyword, keyword, keyword)
                .stream()
                .map(AdminConverter::toMemberDto)
                .collect(Collectors.toList());
    }

    public List<AdminProductResponseDto> filterProducts(boolean isSold) {
        return productRepository.findByIsSold(isSold)
                .stream()
                .map(AdminConverter::toProductDto)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getWeeklySummary() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        long recentUsers = memberRepository.countByDateJoinedAfter(weekAgo);
        long recentTransactions = transactionRepository.countByTradeTimeAfter(weekAgo);

        return Map.of(
                "recentUsers", recentUsers,
                "recentTransactions", recentTransactions
        );
    }
}
