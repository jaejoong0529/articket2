package kjj.articket2.transaction.service;

import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import kjj.articket2.transaction.domain.Transaction;
import kjj.articket2.transaction.dto.TransactionResponse;
import kjj.articket2.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    public List<TransactionResponse> getBoughtProducts(CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        Member buyer = memberRepository.findByUsernameWithLock(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        return transactionRepository.findAllByBuyer(buyer).stream()
                .map(transaction -> new TransactionResponse(transaction, true)) // 구매자로 설정
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getSoldProducts(CustomUserDetails userDetails) {
        String username = userDetails.getUsername();
        Member seller = memberRepository.findByUsernameWithLock(username)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        return transactionRepository.findAllBySeller(seller).stream()
                .map(transaction -> new TransactionResponse(transaction, false)) // 판매자로 설정
                .collect(Collectors.toList());
    }
}
