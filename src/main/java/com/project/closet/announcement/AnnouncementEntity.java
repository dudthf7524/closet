package com.project.closet.announcement;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Announcement")
public class AnnouncementEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column
    private String announcementTitle;

    @Column
    private String announcementContent;

    @Column(nullable = false, updatable = false)
    private String writeDate;
}
