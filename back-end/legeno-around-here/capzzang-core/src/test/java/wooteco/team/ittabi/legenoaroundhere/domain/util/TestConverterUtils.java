package wooteco.team.ittabi.legenoaroundhere.domain.util;

import static wooteco.team.ittabi.legenoaroundhere.domain.util.constants.ImageConstants.TEST_IMAGE_DIR;

import io.restassured.internal.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class TestConverterUtils {

    public static MultipartFile convert(String fileName, String contentType) throws IOException {
        File file = new File(TEST_IMAGE_DIR + fileName);
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(fileName, file.getName(), contentType,
            IOUtils.toByteArray(input));
    }
}
