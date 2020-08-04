package wooteco.team.ittabi.legenoaroundhere.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.aws.S3Uploader;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.ImageExtension;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotFoundAlgorithmException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;

@Slf4j
@Transactional
@Service
public class ImageService {

    public static final String IMAGE_TYPE = "image";
    public static final String IMAGE_DIR = "posts/images/";
    public static final String ALGORITHM_NAME = "MD5";

    private final S3Uploader s3Uploader;

    public ImageService(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    public Image upload(MultipartFile multipartFile) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validateImage(multipartFile);
        String imageUrl = s3Uploader
            .upload(multipartFile, IMAGE_DIR + calculateUserHashCode(user));
        System.out.println(IMAGE_DIR + calculateUserHashCode(user));
        return Image.builder()
            .name(multipartFile.getName())
            .url(imageUrl)
            .build();
    }

    private String calculateUserHashCode(User user) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM_NAME);
            final byte[] bytes = String.valueOf(user.getId()).getBytes();
            Objects.requireNonNull(messageDigest).update(bytes);
            byte[] digest = messageDigest.digest();
            return DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            log.error(ALGORITHM_NAME + "에 해당하는 알고리즘이 없음");
            throw new NotFoundAlgorithmException(ALGORITHM_NAME + "에 해당하는 알고리즘이 없습니다!");
        }
    }

    private void validateImage(MultipartFile multipartFile) {
        ImageExtension.validateImageExtension(multipartFile);
        validateImageMimeType(multipartFile);
    }

    private void validateImageMimeType(MultipartFile multipartFile) {
        try {
            if (isNotImageMimeType(multipartFile)) {
                log.debug("이미지 MIME 타입이 아님, originalFileName = {}",
                    multipartFile.getOriginalFilename());
                throw new NotImageMimeTypeException(
                    multipartFile.getName() + " 파일은 이미지 MIME 타입이 아닙니다!");
            }
        } catch (IOException e) {
            log.error("의 바이트 파일을 가져오는데 실패했습니다!");
        }
    }

    private boolean isNotImageMimeType(MultipartFile multipartFile) throws IOException {
        Tika tika = new Tika();
        return !tika.detect(multipartFile.getBytes())
            .startsWith(IMAGE_TYPE);
    }

}
