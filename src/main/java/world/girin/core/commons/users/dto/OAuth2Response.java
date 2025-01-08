package world.girin.core.commons.users.dto;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getNickname();

    String getProfileImage();

}