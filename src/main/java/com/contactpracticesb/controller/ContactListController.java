package com.contactpracticesb.controller;

import com.contactpracticesb.dto.ContactListForm;
import com.contactpracticesb.entity.ContactList;
import com.contactpracticesb.repository.ContactListRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j //로깅
public class ContactListController {

    @Autowired
    private ContactListRepository contactListRepository;

    @GetMapping("getList")
    public String getList(){
        return "getList";
    }

    @GetMapping("add")
    public String add(){
        return "add";
    }

    @PostMapping("create")
    public String addContact(ContactListForm form){

        log.info(form.toString());

        //dto를 entity로 변환. 폼을가지고 toEntity를 호출해서 ContactList 타입에 엔티티를 반환해오는 형식
        ContactList contactList = form.toEntity();

        log.info(form.toString());

        //리파지토리에게 엔티티를 디비에 저장하게 함
        ContactList saved = contactListRepository.save(contactList); // 만든 ContactList를 등록. 등록된 데이터를 ContactList타입으로 반환하도록함.
        log.info(saved.toString());

        return "add";
        //return "redirect:/getList/detail/" + saved.getId();

    }

    @GetMapping("/getList/detail/{id}")
    public String detail(@PathVariable Long id, Model model){
        log.info("id: "+id);

       //id로 데이터를 가져옴
        ContactList contactListEntity = contactListRepository.findById(id).orElse(null);

        //가져온 데이터를 모델에 등록
        model.addAttribute("contactList",contactListEntity);

        log.info("contactList 출력" + contactListEntity);

        //보여줄 페이지를 설정
        return "getList/detail";
    }

    @GetMapping("/getList/list")
    public String index(Model model){

        //모든 연락처를 가져온다
         List<ContactList> contactEntityListView = contactListRepository.findAll();

        // 가져온 연락처를 묶음을 뷰로 전달
        model.addAttribute("contactListView", contactEntityListView);

        log.info("contactListView  출력: "+contactEntityListView);

        return "getList/list";

    }

    @GetMapping("/getList/{id}/update")
    public String edit(@PathVariable Long id, Model model){

        //수정할 데이터 가져오기
        ContactList contactListEntity = contactListRepository.findById(id).orElse(null);

        //모델에 데이터 등록
        model.addAttribute("contactList", contactListEntity);

        return "getList/edit";
    }

    @PostMapping("/getList/update")
    public String update(ContactListForm form){

        log.info(form.toString());
        // dto를 엔디디로 변환
        ContactList contactListEntity = form.toEntity();
        log.info(contactListEntity.toString());

        //엔티티를 디비로 저장
        //디비에서 기존 데이터를 가져옴
        ContactList target = contactListRepository.findById(contactListEntity.getId()).orElse(null);

        //기존데이터가 있을때 데이터갱신
        if(target != null){
            contactListRepository.save(contactListEntity); //엔티티가 디비로 갱신
        }

        return "redirect:/getList/detail/" + contactListEntity.getId();
    }

    @GetMapping("/getList/{id}/delete")
    public String delete(@PathVariable Long id){
        log.info("삭제요청이 들어옴");
        //삭제대상을 기재한다
        ContactList target = contactListRepository.findById(id).orElse(null);
        log.info(target.toString());

        // 대상을 삭제한다
        if(target != null){
            contactListRepository.delete(target);
        }
        //결과페이지
        return "redirect:/getList/list";
    }

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        List<ContactList> contactEntityList = contactListRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ContactList");

        Row row = null;
        Cell cell = null;
        int rowNum = 0;

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("순서");
        cell = row.createCell(1);
        cell.setCellValue("이름");
        cell = row.createCell(2);
        cell.setCellValue("전화번호");
        cell = row.createCell(3);
        cell.setCellValue("이메일");

        for(int i=0; i<contactEntityList.size(); i++){
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(contactEntityList.get(i).id);
            cell = row.createCell(1);
            cell.setCellValue(contactEntityList.get(i).name);
            cell = row.createCell(2);
            cell.setCellValue(contactEntityList.get(i).phoneNum);
            cell = row.createCell(3);
            cell.setCellValue(contactEntityList.get(i).email);
        }

        // 컨텐츠 타입과 파일명 지정
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename=ContactList.xlsx");

        // Excel File Output
        workbook.write(response.getOutputStream());
        workbook.close();

    }

}
