package com.project.closet.announcement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    public List<AnnouncementEntity> announcementList() {
        return announcementRepository.findAll();
    }

    public void announcementSave(AnnouncementEntity announcement) {
        announcementRepository.save(announcement);
    }

    public Optional<AnnouncementEntity> findById(Long id) {

        return announcementRepository.findById(id);
    }

    public void deleteById(Long id) {
        announcementRepository.deleteById(id);

    }

    public List<AnnouncementEntity> findByAnnouncementTitle(String announcementTitle) {
        return announcementRepository.findByAnnouncementTitle(announcementTitle);
    }
}
