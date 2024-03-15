package ssafy.GeniusOfInvestment.user.dto;

import lombok.Builder;
import lombok.Getter;
import ssafy.GeniusOfInvestment.entity.User;

@Getter
public class SecurityUserDto {

    private final Long id;
    private final String socialId;
    private final String name;
    private final String nickName;
    private final int imageId;

    @Builder
    private SecurityUserDto(Long id, String socialId, String name, String nickName, int imageId) {
        this.id = id;
        this.socialId = socialId;
        this.name = name;
        this.nickName = nickName;
        this.imageId = imageId;
    }

    public static SecurityUserDto from(User user) {
        return builder()
                .id(user.getId())
                .socialId(user.getSocialId())
                .nickName(user.getNickName())
                .imageId(user.getImageId())
                .build();
    }
}
