package kjj.articket2.member.controller;

import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.MemberConverter;
import kjj.articket2.member.dto.*;
import kjj.articket2.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberConverter memberConverter;
    @PostMapping("/findUsername")
    public ResponseEntity<String> findUsername(@RequestBody MemberFindUsernameRequest request) {
        memberService.findUsername(request);
        return ResponseEntity.ok("아이디가 이메일로 전송되었습니다.");
    }

    @PostMapping("/findPassword")
    public ResponseEntity<String> findPassword(@RequestBody MemberFindPasswordRequest request) {
        memberService.findPassword(request);
        return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody MemberChangePasswordRequest request) {
        memberService.changePassword(request);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    @PostMapping("/recharge")
    public ResponseEntity<String> rechargeMoney(@RequestBody MoneyRechargeRequest request,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.rechargeMoney(request, userDetails);
        return ResponseEntity.ok("충전이 완료되었습니다.");
    }

    @GetMapping("/me")
    public ResponseEntity<MemberDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(memberConverter.toDto(userDetails));
    }
}
