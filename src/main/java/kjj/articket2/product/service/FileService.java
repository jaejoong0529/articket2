package kjj.articket2.product.service;

import kjj.articket2.product.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class FileService {
    private final String uploadDir = "uploads/"; // 이미지 저장 폴더

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new FileStorageException("파일명 또는 확장자가 올바르지 않습니다.");
        }

        // 확장자 화이트리스트 체크
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!List.of(".jpg", ".jpeg", ".png", ".gif").contains(fileExtension)) {
            throw new FileStorageException("허용되지 않은 파일 형식입니다.");
        }

        String fileName = UUID.randomUUID() + fileExtension;
        Path filePath = Path.of(uploadDir, fileName);

        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new FileStorageException("파일 저장 실패", e);
        }
    }
}
