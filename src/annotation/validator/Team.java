package annotation.validator;

public class Team {
    @NotEmpty(message = "이름이 비었습니다.")
    private String name;

    @Range(min = 1,max = 100,message = "1과 100사이어야 합니다.")
    private int memberCount;

    public Team(String name, int memberCount) {
        this.name = name;
        this.memberCount = memberCount;
    }

    public String getName() {
        return name;
    }

    public int getMemberCount() {
        return memberCount;
    }
}
