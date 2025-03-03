package kjj.articket2.member.controller;

import jakarta.servlet.http.HttpSession;
import kjj.articket2.auth.dto.MemberSignUpRequest;
import kjj.articket2.member.dto.*;
import kjj.articket2.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
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

}