package wooteco.team.ittabi.legenoaroundhere.utils;

import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageTestConstants.TEST_IMAGE_DIR;

import io.restassured.internal.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileConverter {

    public static MultipartFile convert(String fileName, String contentType) throws IOException {
        File file = new File(TEST_IMAGE_DIR + fileName);
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(fileName, file.getName(), contentType,
            IOUtils.toByteArray(input));
    }
}
