package an.answering.controller;

import an.answering.domain.Authority;
import an.answering.domain.User;
import an.answering.service.AuthorityService;
import an.answering.service.UserService;
import an.answering.util.ConstraintViolationExceptionHander;
import an.answering.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 2017/8/17.
 */
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    /**
     * 查询所有用户
     * @return
     */
    @GetMapping
    public ModelAndView list(@RequestParam(value="async",required=false) boolean async,
                             @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                             @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                             @RequestParam(value="name",required=false,defaultValue="")String name,
                             Model model){

        Pageable pageable = new PageRequest(pageIndex,pageSize);
        Page<User> page = userService.listUserByNameLise(name,pageable);
        List<User> list = page.getContent();

        model.addAttribute("page",page);
        model.addAttribute("userList",list);
        return  new ModelAndView(async==true?"users/list :: #mainContainerReplace":"users/list","userModel",model);
    }

    /**
     * 获取表单页面
     */

    @GetMapping("/add")
    public ModelAndView createForm(Model model){
        model.addAttribute("user",new User(null,null,null,null));
        return new ModelAndView("users/add","userModel",model);
    }

    /**
     * 新建用户
     *
     */

    @PostMapping
    public ResponseEntity<Response> create(User user,Long authorityId){
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(authorityId));
        user.setAuthorities(authorities);
        if (user.getId() == null){
            //加密密码
            user.setEncodePassword(user.getPassword());
        }else {
            //判断密码是否变更
            User orginalUser = userService.getUserById(user.getId());
            String rawPassword = orginalUser.getPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePassword = encoder.encode(user.getPassword());
            boolean isMatch = encoder.matches(rawPassword,encodePassword);
            if (!isMatch){
                user.setEncodePassword(user.getPassword());
            }
        }
        try {
            userService.saveOrUpdateUser(user);
        }catch (javax.validation.ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHander.getMessage(e)));
        }


        return ResponseEntity.ok().body(new Response(true,"处理成功",user));
    }


    /**
     * 删除用户
     *
     */

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id,Model model){
        try {
            userService.removeUser(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }

    /**
     * 获取修改用户的界面以及数据
     */
    @GetMapping(value = "/edit/{id}")
    public ModelAndView modifyForm(@PathVariable("id")Long id,Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        return new ModelAndView("users/edit","userModel",model);
    }

    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")  //判断username是否当前用户
    public ModelAndView profile(@PathVariable("username")String username,Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);//文件服务器地址
        return new ModelAndView("/userspace/profile","userModel",model);
    }

    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username")String username,User user){
        User orginalUser = userService.getUserById(user.getId());
        orginalUser.setEmail(user.getEmail());
        orginalUser.setName(user.getName());

        //判断密码是否做了更改
        String rawPassword = orginalUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodPassword = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword,encodPassword);
        if (!isMatch){
            orginalUser.setEncodePassword(user.getPassword());
        }

        userService.saveOrUpdateUser(orginalUser);
        return "redirect:/u/"+username + "/profile";

    }



}
