package wooteco.team.ittabi.legenoaroundhere.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.xml.bind.DatatypeConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.aws.S3Uploader;
import wooteco.team.ittabi.legenoaroundhere.config.IAuthenticationFacade;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.post.image.ImageExtension;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;
import wooteco.team.ittabi.legenoaroundhere.exception.NotFoundAlgorithmException;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;

@Slf4j
@Transactional
@Component
@AllArgsConstructor
public class ImageUploader {

    public static final String IMAGE_TYPE = "image";
    public static final String IMAGE_DIR = "posts/images/";
    public static final String ALGORITHM_NAME = "MD5";

    private final S3Uploader s3Uploader;
    private final IAuthenticationFacade authenticationFacade;

    public Image uploadImage(MultipartFile multipartFile) {
        User user = (User) authenticationFacade.getPrincipal();
        validateImage(multipartFile);
        String imageUrl = s3Uploader
            .upload(multipartFile, IMAGE_DIR + calculateUserHashCode(user));
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
