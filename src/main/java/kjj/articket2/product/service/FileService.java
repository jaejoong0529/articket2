package kjj.articket2.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class FileService {
    private final String uploadDir = "uploads/"; // 이미지 저장 폴더

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            // 파일 확장자 유지
            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = UUID.randomUUID() + fileExtension; // 유니크한 파일명 생성
            Path filePath = Path.of(uploadDir + fileName);

            // 디렉토리 생성 (없다면)
            Files.createDirectories(filePath.getParent());

            // 파일 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName; // 저장된 파일 URL 반환
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
