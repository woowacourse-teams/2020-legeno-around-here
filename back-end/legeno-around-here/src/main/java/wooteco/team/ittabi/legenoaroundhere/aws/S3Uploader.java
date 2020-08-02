package wooteco.team.ittabi.legenoaroundhere.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3Uploader {

    public static final String DIR_DELIMITER = "/";
    private static final Logger logger = LoggerFactory.getLogger(S3Uploader.class);

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
            logger.info("파일이 삭제되었습니다.");
        } else {
            logger.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) {
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
        File convertFile = new File(originalFileName);
        try {
            if (convertFile.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            logger.info("바이트 파일을 가져오는데 실패했습니다!");
        }
        return Optional.empty();
    }
}
