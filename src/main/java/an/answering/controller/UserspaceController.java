package an.answering.controller;

import an.answering.domain.Blog;
import an.answering.domain.User;
import an.answering.service.BlogService;
import an.answering.service.UserService;
import an.answering.util.ConstraintViolationExceptionHander;
import an.answering.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * Created by HP on 2017/8/17.
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BlogService blogService;

    @Value("${file.server.url}")
    private String fileServerUrl;


    /**
     * 用户主页
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username")String username,Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("user",user);
        return "redirect:/u/" + username + "/blogs";
    }

    /**
     * 获取博客列表
     * @param username
     * @param order
     * @param category
     * @param keyword
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username")String username,
                                   @RequestParam(value="order",required = false,defaultValue = "new")String order,
                                   @RequestParam(value = "category",required = false)Long category,
                                   @RequestParam(value = "keyword",required = false)String keyword,
                                   @RequestParam(value = "async",required = false)boolean async,
                                   @RequestParam(value = "pageIndex",required = false,defaultValue = "0")int pageIndex,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
                                   Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        Page<Blog> page = null;
        if (category != null && category > 0){//分类查询
            return "/userspace/u";
        }else if (order.equals("hot")){//最热查询
            Sort sort = new Sort(Sort.Direction.DESC,"reading","comments","likes");
            Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
            page = blogService.listBlogByTitleVoteAndSort(user,keyword,pageable);
        }else if (order.equals("new")){//最新查询
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = blogService.listBlogByTitleVote(user,keyword,pageable);
        }

        List<Blog> list = page.getContent();
        model.addAttribute("user",user);
        model.addAttribute("order",order);
        model.addAttribute("catalogId",category);
        model.addAttribute("keyword",keyword);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);
        return (async == true ? "/userspace/u :: #mainContainerRepleace" : "/userspace/u" );
    }

    /**
     * 获取博客展示页面
     *
     * @param id
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String listBlgsByOrder(@PathVariable("id")Long id,
                                  @PathVariable("username")String userrname,
                                  Model model){
       // User principaal = null;
        Blog blog = blogService.getBlogById(id);

        //每次读取，阅读量+1
        blogService.readingIncrease(id);
        boolean isBlogOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")){
            User principaal = (User) SecurityContextHolder.getContext().getAuthentication();
            if (principaal != null && userrname.equals(principaal.getUsername())){
                isBlogOwner = true;
            }
        }
        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blogService.getBlogById(id));
        return "/userspace/blog";
    }

    @GetMapping("/{username}/blogs/edit")
    public String editBlog(){
        return "/userspace/blogedit";
    }
    /**
     * 获取新增博客的界面
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username")String username,Model model){
        model.addAttribute("blog",new Blog(null,null,null));
        model.addAttribute("fileServerUrl",fileServerUrl);//文件服务器的地址
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 获取编辑博客的界面
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username")String username,@PathVariable("id")Long id, Model model){
        model.addAttribute("blog",blogService.getBlogById(id));
        model.addAttribute("fileServerUrl",fileServerUrl);
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 保存博客
     *
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username")String username,
                                            @RequestBody Blog blog ){
        User user = (User) userDetailsService.loadUserByUsername(username);
        blog.setUser(user);
        try {
            blogService.saveBlog(blog);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHander.getMessage(e)));
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl = "/u/" + username + "/blog/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }


    /**
     *
     * 删除博客
     */
    @DeleteMapping("/{username}/blog/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username")String username,
                                        @PathVariable("id")Long id ){
        try {
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

    /**
     * 获取编辑头像的界面
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username")String username, Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user","user");
        return new ModelAndView("/userspace/avatar","userModel",model);
    }

    /**
     * 保存头像
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username")String userame,
                                               @RequestBody User user){
        String avatarUrl = user.getAvatar();
        User originalUser = userService.getUserById(user.getId());
        originalUser.setAvatar(avatarUrl);
        userService.saveOrUpdateUser(originalUser);

        return ResponseEntity.ok().body(new Response(true,"处理成功",avatarUrl));
    }
}
