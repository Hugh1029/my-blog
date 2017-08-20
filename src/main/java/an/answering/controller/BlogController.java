package an.answering.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by HP on 2017/8/17.
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {


    @GetMapping
    public String listBlog(@RequestParam(value="order",required = false,defaultValue ="new")String order,
                           @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword){
        System.out.println("order:" + order + ";tag:" + keyword);
        return "redirect:/index?order="+order+"&keyword="+keyword;
    }
}
