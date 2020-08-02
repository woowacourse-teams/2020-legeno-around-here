package wooteco.team.ittabi.legenoaroundhere.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.exception.FileIOException;

@Slf4j
@Component
public class S3Uploader {

    public static final String DIR_DELIMITER = "/";

    private final AmazonS3 amazonS3;
    private final String bucket;

    public S3Uploader(AmazonS3 amazonS3, String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    public String upload(MultipartFile multipartFile, String dirName) {
        File uploadFile = convert(multipartFile)
            .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + DIR_DELIMITER + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile multipartFile) {
        String originalFileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
        File convertFile = new File(originalFileName);
        try {
            if (convertFile.createNewFile()) {
                FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
                fileOutputStream.write(multipartFile.getBytes());
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            log.error("File 읽기 실패, originalFileName = {}", originalFileName);
            throw new FileIOException("File을 읽고 쓰는데 실패했습니다!");
        }
        return Optional.empty();
    }
}
