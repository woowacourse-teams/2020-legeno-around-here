package wooteco.team.ittabi.legenoaroundhere.dto;

import wooteco.team.ittabi.legenoaroundhere.domain.user.User;

public class UserResponse {

    private Long id;
    private String email;
    private String nickname;

    public UserResponse(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmailByString(), user.getNicknameByString());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }
}
