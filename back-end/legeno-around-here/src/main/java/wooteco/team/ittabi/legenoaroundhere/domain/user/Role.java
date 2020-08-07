package wooteco.team.ittabi.legenoaroundhere.domain.user;

public enum Role {
    USER("ROLE_USER"),
    REPORTED_USER("ROLE_REPORTED_USER"),
    ADMIN("ROLE_ADMIN");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }
}
