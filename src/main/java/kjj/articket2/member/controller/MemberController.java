package kjj.articket2.member.controller;

import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody MemberSignUpRequest request) {
        memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<MemberLoginResponse> login(@RequestBody MemberLoginRequest request, HttpSession session) {
        MemberLoginResponse response = memberService.login(request, session);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        memberService.logout(session);
        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/findUsername")
    public ResponseEntity<MemberFindUsernameResponse> findUsername(@RequestBody MemberFindUsernameRequest request) {
        MemberFindUsernameResponse response = memberService.findUsername(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/findPassword")
    public ResponseEntity<MemberFindPasswordResponse> findPassword(@RequestBody MemberFindPasswordRequest request) {
        MemberFindPasswordResponse response = memberService.findPassword(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/changePassword")
    public ResponseEntity<MemberChangePasswordResponse> changePassword(@RequestBody MemberChangePasswordRequest request) {
        MemberChangePasswordResponse response = memberService.changePassword(request);
        return ResponseEntity.ok(response);
    }

}


