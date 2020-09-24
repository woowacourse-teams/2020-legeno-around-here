package wooteco.team.ittabi.legenoaroundhere.controller.validator;

import java.util.List;
import java.util.Objects;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageConstraintValidator implements
    ConstraintValidator<ImagesConstraint, List<MultipartFile>> {

    public static final int MAX_IMAGE_MB_VOLUME = 5;
    public static final int MAX_IMAGE_LENGTH = 10;
    private static final int IEC_PREFIX_UNIT = 1024;

    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext context) {
        if (Objects.isNull(images)) {
            addConstraintViolation(context, "이미지가 비어있습니다!");
            return false;
        }

        if (isOverImagesLength(images)) {
            addConstraintViolation(context, "이미지는 최대 " + MAX_IMAGE_LENGTH + "개까지 등록할 수 있습니다!");
            return false;
        }

        if (isOverImagesSize(images)) {
            addConstraintViolation(context, "이미지는 " + MAX_IMAGE_MB_VOLUME + "MB를 넘을 수 없습니다!");
            return false;
        }
        return true;
    }

    private boolean isOverImagesSize(List<MultipartFile> images) {
        return images.stream()
            .anyMatch(this::isOverImageSize);
    }

    private boolean isOverImageSize(MultipartFile image) {
        double accurateImageSize = image.getSize();
        double imageMbSize = convertMbSize(accurateImageSize);
        return imageMbSize > MAX_IMAGE_MB_VOLUME;
    }

    private double convertMbSize(double accurateImageSize) {
        return accurateImageSize / IEC_PREFIX_UNIT / IEC_PREFIX_UNIT;
    }

    private boolean isOverImagesLength(List<MultipartFile> images) {
        return images.size() > MAX_IMAGE_LENGTH;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
            .addConstraintViolation();
    }
}
