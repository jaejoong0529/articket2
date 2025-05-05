package kjj.articket2.admin.service;

import kjj.articket2.admin.AdminConverter;
import kjj.articket2.admin.dto.AdminMemberResponseDto;
import kjj.articket2.admin.dto.AdminProductResponseDto;
import kjj.articket2.admin.dto.AdminTransactionResponseDto;
import kjj.articket2.bid.repository.BidRepository;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.product.repository.ProductRepository;
import kjj.articket2.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AdminService {
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final BidRepository bidRepository;
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
        memberRepository.deleteById(memberId);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public void deleteTransaction(Long transactionId) {
        transactionRepository.deleteById(transactionId);
    }
}
