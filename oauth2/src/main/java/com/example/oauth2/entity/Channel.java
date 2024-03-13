package com.example.oauth2.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", columnDefinition = "INT UNSIGNED")
    private Long id;

    private String chName;

    //-----------------------------------------------------
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    public void addRooms(Room room){
        rooms.add(room);
        room.setChannel(this);
    }
    public void addParticipants(User user){
        participants.add(user);
        user.setChannel(this);
    }
}
