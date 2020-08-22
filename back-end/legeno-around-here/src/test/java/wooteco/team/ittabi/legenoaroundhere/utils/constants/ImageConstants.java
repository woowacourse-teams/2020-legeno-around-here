package wooteco.team.ittabi.legenoaroundhere.utils.constants;

import java.io.File;
import java.util.Collections;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ImageConstants {

    public static final List<MultipartFile> TEST_IMAGE_EMPTY_MULTIPART_FILES
        = Collections.emptyList();
    public static final String TEST_IMAGE_DIR
        = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "static"
        + File.separator + "images" + File.separator;
    public static final String TEST_IMAGE_TEXT_CONTENT_TYPE = "text/plain";
    public static final String TEST_IMAGE_CONTENT_TYPE = "image/jpg";
    public static final String TEST_IMAGE_NAME = "image1.jpg";
    public static final String TEST_IMAGE_OTHER_NAME = "image2.jpg";
    public static final String TEST_IMAGE_NOT_MIME_TYPE_NAME = "not_mime_type_image.jpg";
    public static final String TEST_IMAGE_NOT_TXT_EXTENSION = "not_image_extension.txt";
}
