package com.project.closet.announcement;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {

    Page<AnnouncementEntity> findPageBy(Pageable page);

    Page<AnnouncementEntity> findByAnnouncementTitleContaining(String title, Pageable pageable);
    List<AnnouncementEntity> findByAnnouncementTitle(String announcementTitle);
}
