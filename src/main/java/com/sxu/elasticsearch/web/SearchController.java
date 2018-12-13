package com.sxu.elasticsearch.web;

import com.sxu.elasticsearch.entity.Page;
import com.sxu.elasticsearch.entity.QueryDTO;
import com.sxu.elasticsearch.entity.Software;
import com.sxu.elasticsearch.repository.IRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class SearchController {

    @Autowired
    private IRepository SoftwareRepository;

    @GetMapping("/")
    public String index(Model model) {
        QueryDTO queryDTO = QueryDTO.builder().minId(1.0f).orderBy("id").build();
        Page<Software> page = SoftwareRepository.query(queryDTO, 1, 6);
        List<String> recommendWord = new ArrayList<>();
        if (page != null) {
            page.getList().forEach(Software -> {
                String word = Software.getSoftware();
                String title = Software.getCopyright();

                recommendWord.add(word);
            });
        }
        model.addAttribute("recommendWord", recommendWord);
        return "index";
    }

    @RequestMapping("/s")
    public String search(
            @RequestParam("wd") String queryString,
            @RequestParam(value = "pn", required = false, defaultValue = "1") int pageNo,
            Model model
    ) {
        log.info("搜索参数wd:{},pn:{}", queryString, pageNo);
        Page<Software> page = SoftwareRepository.query(queryString, pageNo, 10);
        model.addAttribute("page", page);
        model.addAttribute("wd", queryString);
        return "search";
    }

}
