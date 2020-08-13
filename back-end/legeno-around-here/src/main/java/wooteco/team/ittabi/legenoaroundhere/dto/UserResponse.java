package wooteco.team.ittabi.legenoaroundhere.dto;

import static java.time.LocalDateTime.now;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode(exclude = "image")
@ToString
public class UserResponse {

    private Long id;
    private String email;
    private String nickname;
    private UserImageResponse image;
    private AreaResponse area;

    public static UserResponse from(User user) {
        List<String> urls = new ArrayList<>();
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/1.jpg");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/2.jpeg");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/3.jpeg");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/4.jpg");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/5.png");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/6.jpeg");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/7.jpeg");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/8.jpg");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/9.jpg");
        urls.add(
            "https://legeno-around-here.s3.ap-northeast-2.amazonaws.com/users/images/mock/10.jpg");
        Collections.shuffle(urls);

        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmailByString())
            .nickname(user.getNicknameByString())
            .image(new UserImageResponse(1L, "mock_profile_image", urls.get(0), now(), now()))
            .area(AreaResponse.of(user.getArea()))
            .build();
    }
}
