package wooteco.team.ittabi.legenoaroundhere.domain.user;

public enum UserRole {
    USER("USER"),
    REPORTED_USER("REPORTED_USER"),
    ADMIN("ADMIN");

    private String roleName;

    UserRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return this.roleName;
    }
}
