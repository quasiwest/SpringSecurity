package com.example.oauth2.entity;

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
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "friend_list_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    //연관 관계의 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "my_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private User friend;

    //0이면 승인 대기, 1이면 승인, 2이면 거절(거절이면 그냥 db에서 삭제하는것도 괜찮을수도)
    @Column(columnDefinition = "TINYINT(2) default 0")
    private int isApprove;

    //-----------------------------------------------------------------
    @OneToMany(mappedBy = "chatId", cascade = CascadeType.ALL)
    private List<ChatRecord> records = new ArrayList<>();

    public void addMsg(ChatRecord msg){
        records.add(msg);
        msg.setChatId(this);
    }
}
