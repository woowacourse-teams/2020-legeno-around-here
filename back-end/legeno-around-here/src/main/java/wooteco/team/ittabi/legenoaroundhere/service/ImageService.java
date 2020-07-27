package wooteco.team.ittabi.legenoaroundhere.service;

import java.io.IOException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.domain.Image;
import wooteco.team.ittabi.legenoaroundhere.domain.ImageExtension;
import wooteco.team.ittabi.legenoaroundhere.domain.Post;
import wooteco.team.ittabi.legenoaroundhere.exception.NotImageMimeTypeException;
import wooteco.team.ittabi.legenoaroundhere.repository.ImageRepository;

@Transactional
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image save(MultipartFile multipartFile, Post post) {
        validateImage(multipartFile);

        return imageRepository.save(new Image(multipartFile.getName(), "mockUrl", post));
    }

    private void validateImage(MultipartFile multipartFile) {
        ImageExtension.validateImageExtension(multipartFile);
        validateImageMimeType(multipartFile);
    }

    private void validateImageMimeType(MultipartFile multipartFile) {
        try {
            if (isNotImageMimeType(multipartFile)) {
                throw new NotImageMimeTypeException(
                    multipartFile.getName() + " 파일은 이미지 MIME 타입이 아닙니다!");
            }
        } catch (IOException e) {
            // TODO: 2020/07/28 추후 logger 적용
            System.out.println("바이트 파일을 가져오는데 실패했습니다!");
        }
    }

    private boolean isNotImageMimeType(MultipartFile multipartFile) throws IOException {
        Tika tika = new Tika();

        return !tika.detect(multipartFile.getBytes())
            .startsWith("image");
    }
}
