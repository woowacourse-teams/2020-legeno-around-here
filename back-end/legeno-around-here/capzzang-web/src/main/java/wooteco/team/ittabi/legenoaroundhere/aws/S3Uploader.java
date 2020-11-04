package wooteco.team.ittabi.legenoaroundhere.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.exception.FileIOException;
import wooteco.team.ittabi.legenoaroundhere.exception.MultipartFileConvertException;

@Component
@AllArgsConstructor
@Slf4j
public class S3Uploader {

    private static final String FILE_NAME_DELIMITER = "_";

    private final AmazonS3 amazonS3Client;
    private final String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        File uploadFile = convert(multipartFile);
        return upload(uploadFile, dirName);
    }

    private File convert(MultipartFile multipartFile) {
        String originalFileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
        try {
            return convertToCreateFile(multipartFile, originalFileName);
        } catch (IOException e) {
            throw new FileIOException(originalFileName + " File을 읽고 쓰는데 실패했습니다!");
        }
    }

    private File convertToCreateFile(MultipartFile multipartFile, String originalFileName)
        throws IOException {
        File convertFile = new File(originalFileName);
        if (convertFile.createNewFile()) {
            return createFile(multipartFile, convertFile);
        }
        throw new MultipartFileConvertException(originalFileName + "파일이 MultipartFile -> File로 전환이 실패했습니다.");
    }

    private File createFile(MultipartFile multipartFile, File convertFile) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
            fileOutputStream.write(multipartFile.getBytes());
            return convertFile;
        }
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = generateFileName(uploadFile, dirName);
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String generateFileName(File uploadFile, String dirName) {
        return dirName
            + File.separator
            + UUID.randomUUID().toString()
            + FILE_NAME_DELIMITER
            + uploadFile.getName();
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        try {
            Files.delete(targetFile.toPath());
        } catch (IOException e) {
            throw new FileIOException(targetFile.getName() + " 파일이 삭제되지 못했습니다.");
        }
    }
}
