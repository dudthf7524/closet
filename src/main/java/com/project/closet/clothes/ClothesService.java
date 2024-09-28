package com.project.closet.clothes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final ClothesRepository clothesRepository;
    public void save(ClothesEntity clothesEntity) {
        clothesRepository.save(clothesEntity);
    }


    public List<ClothesEntity> findAllByCategorycodelist(Long categorycode, Long memberNo) {


        return  clothesRepository.findByCategorycodeAndMemberNo(categorycode, memberNo);
    }

    public Optional<ClothesEntity> detail(Long id) {



        return clothesRepository.findById(id);

    }

    public void edit(ClothesEntity clothesEntity) {
        clothesRepository.save(clothesEntity);

    }

    public void deleteById(Long id) {

        clothesRepository.deleteById(id);
    }

    public List<ClothesEntity> findAll(Long memberNo) {


        return clothesRepository.findByMemberNoWithCategoryOrderByCategorycodeAsc(memberNo);

    }


    public List<ClothesEntity> findByName(Long memberNoMethod, String name) {
        return clothesRepository.findByNameContainsAndMemberNo(name, memberNoMethod);
    }
}
