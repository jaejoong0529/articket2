package kjj.articket2.member.service;

import jakarta.servlet.http.HttpSession;
import kjj.articket2.member.MemberConverter;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.dto.*;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.exception.InvalidNicknameException;
import kjj.articket2.member.exception.InvalidPasswordException;
import kjj.articket2.member.exception.InvalidUsernameException;
import kjj.articket2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    //회원가입
    public void signup(MemberSignUpRequest request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new InvalidUsernameException("아이디가 중복입니다.");
        }
        if (memberRepository.existsByNickname(request.getNickname())) {
            throw new InvalidNicknameException("닉네임이 중복입니다.");
        }
        Member member = MemberConverter.fromDto(request).toBuilder()
                .password(passwordEncoder.encode(request.getPassword()))
                .dateJoined(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();
        memberRepository.save(member);
    }
    //로그인
    public MemberLoginResponse login(MemberLoginRequest request, HttpSession session) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + request.getUsername()));
        passwordMatches(request.getPassword(), member.getPassword());
        member.setLastLogin(LocalDateTime.now());
        memberRepository.updateLastLogin(member.getId(), member.getLastLogin());
        session.setAttribute("member", member);
        return MemberConverter.toLoginResponse();

    }

    private void passwordMatches(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    //로그아웃
    public void logout(HttpSession session) {
        session.invalidate();
    }

    //아이디찾기
    public MemberFindUsernameResponse findUsername (MemberFindUsernameRequest request) {
        Member member = memberRepository.findByNameAndEmail(request.getName(), request.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        sendUsernameEmail(request.getEmail(), member.getUsername());
        return MemberConverter.toFindUsernameResponse(member);
    }

    private void sendUsernameEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("아이디 찾기 결과");
        message.setText("회원님의 아이디는 " + username + "입니다.");
        javaMailSender.send(message);
    }

    //비밀번호 찾기
    public MemberFindPasswordResponse findPassword(MemberFindPasswordRequest request) {
        Member member = memberRepository.findByNameAndEmail(request.getUsername(), request.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        String tempPassword = generateTempPassword();
        member.setPassword(passwordEncoder.encode(tempPassword));
        memberRepository.save(member);
        sendPasswordEmail(request.getEmail(), tempPassword);
        return MemberConverter.toFindPasswordResponse();
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 랜덤 문자열 생성
    }
    private void sendPasswordEmail(String to, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("비밀번호 찾기 결과");
        message.setText("회원님의 임시 비밀번호는 " + tempPassword + "입니다.\n로그인 후 반드시 비밀번호를 변경해주세요.");
        javaMailSender.send(message);
    }
    //비밀번호 바꾸기
    public MemberChangePasswordResponse changePassword(MemberChangePasswordRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
        return MemberConverter.toChangePasswordResponse();
    }

}
