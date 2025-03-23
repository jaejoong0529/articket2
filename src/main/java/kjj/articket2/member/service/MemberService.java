package kjj.articket2.member.service;

import kjj.articket2.global.jwt.CustomUserDetails;
import kjj.articket2.member.domain.Member;
import kjj.articket2.member.dto.*;
import kjj.articket2.member.exception.InvalidPasswordException;
import kjj.articket2.member.exception.MemberNotFoundException;
import kjj.articket2.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    //아이디찾기
    public void findUsername (MemberFindUsernameRequest request) {
        Member member = memberRepository.findByNameAndEmail(request.getName(), request.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        sendUsernameEmail(request.getEmail(), member.getUsername());
    }

    private void sendUsernameEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("아이디 찾기 결과");
        message.setText("회원님의 아이디는 " + username + "입니다.");
        javaMailSender.send(message);
    }

    //비밀번호찾기
    public void findPassword(MemberFindPasswordRequest request) {
        Member member = memberRepository.findByUsernameAndEmail(request.getUsername(), request.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));

        String tempPassword = generateTempPassword();
        member.setPassword(passwordEncoder.encode(tempPassword));
        memberRepository.save(member);

        sendPasswordEmail(request.getEmail(), tempPassword);

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

    //비밀번호 변경
    public void changePassword(MemberChangePasswordRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        member.setPassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }
    //돈 충전하기
    public void rechargeMoney(MoneyRechargeRequest request, CustomUserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("허용되지 않았습니다");
        }
        String username = userDetails.getUsername(); // 사용자 이름 가져오기
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("일치하는 정보가 없습니다."));
        if (member.getMoney() == null) {
            member.setMoney(0); // 기본값 설정
        }
        member.addMoney(request.getAmount());
    }
}