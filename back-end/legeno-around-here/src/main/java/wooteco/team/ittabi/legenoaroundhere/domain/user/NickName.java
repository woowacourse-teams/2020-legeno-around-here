package wooteco.team.ittabi.legenoaroundhere.domain.user;

import java.util.Objects;

public class NickName {

    private static final int MAX_LENGTH = 10;

    private String nickName;

    public NickName() {
    }

    public NickName(String nickName) {
        validate(nickName);
        this.nickName = nickName;
    }

    private void validate(String nickName) {
        validateNull(nickName);
        validateEmpty(nickName);
        validateSpace(nickName);
        validateLength(nickName);
    }

    private void validateNull(String nickName) {
        if (Objects.isNull(nickName)) {
            throw new IllegalArgumentException("닉네임이 null 입니다.");
        }
    }

    private void validateEmpty(String nickName) {
        if (nickName.isEmpty()) {
            throw new IllegalArgumentException("닉네임이 비어있습니다.");
        }
    }

    private void validateSpace(String nickName) {
        if (nickName.contains(" ")) {
            throw new IllegalArgumentException("닉네임은 공백을 포함할 수 없습니다.");
        }
    }

    private void validateLength(String nickName) {
        if (nickName.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("닉네임은 10글자 이하여야 합니다.");
        }
    }
}
