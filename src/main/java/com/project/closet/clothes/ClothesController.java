package com.project.closet.clothes;

import com.project.closet.alert.MessageDTO;
import com.project.closet.clothescategory.ClothesCategoryEntity;
import com.project.closet.member.CustomUser;
import com.project.closet.member.MemberEntity;
import com.project.closet.s3.S3Service;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clothes")
public class ClothesController {

    private final ClothesUtil clothesUtil;
    private final S3Service s3Service;

    private final ClothesService clothesService;

    public Long memberNoMethod(Authentication auth){
        CustomUser result = (CustomUser)auth.getPrincipal();
        Long memberNo = result.memberno;

        return memberNo;
    }

    @GetMapping("/insert/{categorycode}")
    public String insert(Model model, Authentication auth, @PathVariable("categorycode") Long categorycode ) {

        String category = clothesUtil.typeofcategory(categorycode);

        model.addAttribute("category", category);
        model.addAttribute("categorycode", categorycode);

        return "clothes/insert";
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    public String getURL(@RequestParam(name = "filename") String filename){
        System.out.println(filename);
        var reuslt =  s3Service.createPresignedUrl("clothes/" + filename);
        System.out.println(reuslt);
        return reuslt;
    }

    @PostMapping("/insert")
    public String insertForm(Authentication auth, ClothesEntity clothesEntity, @ModelAttribute MessageDTO messageDTO, Model model){

        if(clothesEntity.getName() == null || clothesEntity.getName().equals("")){
            messageDTO.setMessage("제품이름을 다시 입력해주세요");
            messageDTO.setAlert("작성 실패");
            messageDTO.setRedirectUri("/clothes/insert/"+clothesEntity.getCategorycode());
            model.addAttribute("messageDTO", messageDTO);
            return ".common/alert";
        }else if(clothesEntity.getPrice() == null || clothesEntity.getPrice().equals("")){
            messageDTO.setMessage("가격을 다시 입력해주세요");
            messageDTO.setAlert("작성 실패");
            messageDTO.setRedirectUri("/clothes/insert/"+clothesEntity.getCategorycode());
            model.addAttribute("messageDTO", messageDTO);
            return ".common/alert";
        }else if(clothesEntity.getPurchaseplace() == null || clothesEntity.getPurchaseplace().equals("")){
            messageDTO.setMessage("구매처를 다시 입력해주세요");
            messageDTO.setAlert("작성 실패");
            messageDTO.setRedirectUri("/clothes/insert/"+clothesEntity.getCategorycode());
            model.addAttribute("messageDTO", messageDTO);
            return ".common/alert";
        }else if(clothesEntity.getBuydate() == null || clothesEntity.getBuydate().equals("")){
            messageDTO.setMessage("날짜를 다시 입력해주세요");
            messageDTO.setAlert("작성 실패");
            messageDTO.setRedirectUri("/clothes/insert/"+clothesEntity.getCategorycode());
            model.addAttribute("messageDTO", messageDTO);
            return ".common/alert";
        }


        clothesEntity.setMemberNo(memberNoMethod(auth));
        if(clothesEntity.getClothesfile() == null || clothesEntity.getClothesfile().equals("")){
            clothesEntity.setClothesfile("https://placehold.co/300");
        }
        clothesService.save(clothesEntity);

        return "redirect:/clothes/list/"+clothesEntity.getCategorycode();
    }

    @GetMapping("/list/{categorycode}")
    public String list(@ModelAttribute ClothesEntity clothesEntity, Model model, Authentication auth, @ModelAttribute MessageDTO messageDTO) {

        if (auth == null){
            messageDTO.setMessage("로그인 후 이용해주세요.");
            messageDTO.setAlert("인증 실패");
            messageDTO.setRedirectUri("/member/login");
            model.addAttribute("messageDTO", messageDTO);
            return ".common/alert";
        }


        Integer sum = 0;
        Integer count = 0;
        List<ClothesEntity> clothesEntityList =  clothesService.findAllByCategorycodelist(clothesEntity.getCategorycode(), memberNoMethod(auth));

        if(clothesEntityList.size()!=0){
            for (int i = 0; i<clothesEntityList.size();i++){
                clothesEntityList.get(i).setNo(i+1);
                String pricecomma = String.format("%,d", clothesEntityList.get(i).getPrice());
                clothesEntityList.get(i).setCommaprice(pricecomma);
                sum += clothesEntityList.get(i).getPrice();
            }
            count =  clothesEntityList.get(clothesEntityList.size()-1).getNo();
        }

        String sumcomma = String.format("%,d", sum);



        String category = clothesUtil.typeofcategory(clothesEntity.getCategorycode());



        model.addAttribute("category", category);
        model.addAttribute("clothesEntityList", clothesEntityList);
        model.addAttribute("sum", sumcomma);
        model.addAttribute("count", count);



        return "clothes/list";
    }

    @GetMapping("/detail")
    public ResponseEntity<Map<String, Object>> getFinancialInfo(@ModelAttribute ClothesEntity clothesEntity) {

        Optional<ClothesEntity> optionalClothes = clothesService.detail(clothesEntity.getId());
        ClothesEntity clothes = optionalClothes.get();


        String name;
        String img;
        Long categorycode;


        name =  clothes.getName();
        String price = String.format("%,d", clothes.getPrice())+" 원";
        img = clothes.getClothesfile();
        categorycode = clothes.getCategorycode();

        if (img.equals("")){
            img = "https://placehold.co/300";
        }

        System.out.println("img" + img);
        Map<String, Object> data = new HashMap<>();

        data.put("id", clothesEntity.getId());
        data.put("name", name);
        data.put("price", price);
        data.put("img", img);
        data.put("categorycode", categorycode);

        return ResponseEntity.ok(data);
    }

    @GetMapping("/edit/{id}/{categorycode}")
    public String edit(@ModelAttribute ClothesEntity clothesEntity, Model model) {

        System.out.println("clothesEntity" + clothesEntity.getId());
        System.out.println("categorycode" + clothesEntity.getCategorycode());

        String category = clothesUtil.typeofcategory(clothesEntity.getCategorycode());

        String categoryupdate = category+" (update)";

        model.addAttribute("category", categoryupdate);

        Optional<ClothesEntity> optionalClothes = clothesService.detail(clothesEntity.getId());
        ClothesEntity clothes =  optionalClothes.get();

        model.addAttribute("clothes", clothes);

        return "clothes/edit";
    }

    @PostMapping("/edit")
    public String editForm(@ModelAttribute ClothesEntity clothesEntity, Authentication auth){

        CustomUser customUser = (CustomUser)auth.getPrincipal();
        clothesEntity.setMemberNo(customUser.memberno);
        System.out.println("clothesEntity.getClothesfile()" + clothesEntity.getClothesfile());

        Long categortcode;

        categortcode = clothesEntity.getCategorycode();

        clothesService.edit(clothesEntity);

        return "redirect:/clothes/list/"+categortcode;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@ModelAttribute ClothesEntity clothesEntity){
        System.out.println(clothesEntity.getId());

        clothesService.deleteById(clothesEntity.getId());

        return ResponseEntity.status(200).body("삭제완료");

    }

    @GetMapping("/listall")
        public String listall(Authentication auth,
                              Model model,
                              @RequestParam(value = "name", required = false) String name
    )
    {

        Integer sum = 0;
        Integer count = 0;
        List<ClothesEntity> clothesEntityList =  clothesService.findAll(memberNoMethod(auth));

        if(name == null || name.equals("")){
            clothesEntityList =  clothesService.findAll(memberNoMethod(auth));
        }else{
            clothesEntityList = clothesService.findByName(memberNoMethod(auth), name);
        }


        if(clothesEntityList.size()!=0){
            for (int i = 0; i<clothesEntityList.size();i++){
                clothesEntityList.get(i).setNo(i+1);
                String pricecomma = String.format("%,d", clothesEntityList.get(i).getPrice());
                clothesEntityList.get(i).setCommaprice(pricecomma);
                sum += clothesEntityList.get(i).getPrice();
            }
            count =  clothesEntityList.get(clothesEntityList.size()-1).getNo();
        }

        String sumcomma = String.format("%,d", sum);


        model.addAttribute("clothesEntityList", clothesEntityList);
        model.addAttribute("sum", sumcomma);
        model.addAttribute("count", count);
        model.addAttribute("name", name);
        return "clothes/listall";
    }
}
