package wooteco.team.ittabi.legenoaroundhere.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.exception.FileIOException;

@Slf4j
@Component
public class S3Uploader {

    public static final String DIR_DELIMITER = "/";
    public static final String FILE_NAME_DELIMITER = "_";

    private final AmazonS3 amazonS3Client;
    private final String bucket;

    public S3Uploader(AmazonS3 amazonS3Client, String bucket) {
        this.amazonS3Client = amazonS3Client;
        this.bucket = bucket;
    }

    public String upload(MultipartFile multipartFile, String dirName) {
        File uploadFile = convert(multipartFile);
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = generateFileName(uploadFile, dirName);
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String generateFileName(File uploadFile, String dirName) {
        return dirName
            + DIR_DELIMITER
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
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
            return;
        }
        log.info("파일이 삭제되지 못했습니다.");
    }

    private File convert(MultipartFile multipartFile) {
        String originalFileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
        File file = new File(originalFileName);
        try {
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            log.error("File 읽기 실패, originalFileName = {}", originalFileName);
            throw new FileIOException("File을 읽고 쓰는데 실패했습니다!");
        }

    }
}
