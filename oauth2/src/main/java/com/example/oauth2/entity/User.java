package com.example.oauth2.entity;

import com.example.oauth2.UserRole;
import com.example.oauth2.user.dto.request.SingUpRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    @Column(nullable = false, unique = true)
    private String socialId;

//    @Column(nullable = false, unique = true)
//    private String name;

    @ColumnDefault("0")
    private Long exp;

    @ColumnDefault("0")
    private int imageId;

    @Column(nullable = false, unique = true)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    //---------------------------------------------------------------------
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alram> alrams = new ArrayList<>();
    public void addAlram(Alram alram){
        alrams.add(alram);
        alram.setUser(this);
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Friend> userList = new ArrayList<>();

    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL)
    private List<Friend> friendList = new ArrayList<>();
    public void addFriend(User toUser){ //친구 요청을 추가하는 메소드
        Friend friend = new Friend();
        friend.setFriend(toUser);
        friend.setUser(this);
        friendList.add(friend);
    }

    //----------------------------------------------------------------------

    @Builder
    public User(String socialId, String name, Long exp, int imageId, String nickName) {
        this.socialId = socialId;
        this.name = name;
        this.exp = exp;
        this.name = name;
        this.imageId = imageId;
        this.nickName = nickName;
    }

    public static User of(String socialId, String name, Long exp, int imageId, String nickName) {
        return builder()
                .socialId(socialId)
                .name(name)
                .exp(exp)
                .name(name)
                .imageId(imageId)
                .nickName(nickName)
                .build();
    }

    public static User from(SingUpRequestDto singUpRequestDto) {
        return builder()
                .socialId(singUpRequestDto.getSocialId())
                .name(singUpRequestDto.getName())
                .exp(singUpRequestDto.getExp())
                .name(singUpRequestDto.getName())
                .imageId(singUpRequestDto.getImageId())
                .nickName(singUpRequestDto.getName())
                .build();
    }


    @Transient
    private Set<UserRole> roles = new HashSet<>(Arrays.asList(UserRole.USER)); //user권한으로 초기값 지정

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
