package wooteco.team.ittabi.legenoaroundhere.utils.constants;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ImageConstants {

    public static final List<Long> TEST_EMPTY_IMAGES = Collections.emptyList();
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
