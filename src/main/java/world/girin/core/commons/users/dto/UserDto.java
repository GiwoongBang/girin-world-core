package world.girin.core.commons.users.dto;

import lombok.Builder;
import lombok.Getter;
import world.girin.core.commons.users.UserEntity;

@Getter
public class UserDto {

    String username;

    String role;

    String nickname;

    String profileImg;

    @Builder
    public UserDto(String username, String role, String nickname, String profileImg) {
        this.username = username;
        this.role = role;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }

    public UserDto(UserEntity user) {
        this.username = user.getUsername();
        this.role = user.getRole();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
    }

}
