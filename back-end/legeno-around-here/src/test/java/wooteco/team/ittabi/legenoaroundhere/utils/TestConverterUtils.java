package wooteco.team.ittabi.legenoaroundhere.utils;

import static wooteco.team.ittabi.legenoaroundhere.utils.constants.ImageConstants.TEST_IMAGE_DIR;

import io.restassured.internal.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import wooteco.team.ittabi.legenoaroundhere.dto.PostImageResponse;

public class TestConverterUtils {

    public static List<Long> convertImageIds(List<PostImageResponse> postImageResponses) {
        return postImageResponses.stream()
            .map(PostImageResponse::getId)
            .collect(Collectors.toList());
    }

    public static MultipartFile convert(String fileName, String contentType) throws IOException {
        File file = new File(TEST_IMAGE_DIR + fileName);
        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile(fileName, file.getName(), contentType,
            IOUtils.toByteArray(input));
    }
}
