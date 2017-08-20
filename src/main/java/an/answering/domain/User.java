package an.answering.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**User实体类
 * Created by HP on 2017/8/17.
 */
@Entity
public class User implements UserDetails {

    private static final long serialVersionUID = -2659268054749420743L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Bean验证
    @NotEmpty(message = "姓名不能为空")
    @Size(min = 2,max = 20)
    @Column(nullable = false,length = 20)//映射为字段，值不能为空
    private String name;

    @NotEmpty(message = "邮箱不能为空")
    @Size(max = 50)
    @Email(message = "邮箱格式不对")
    @Column(nullable = false,length = 50,unique = true)
    private String email;

    @NotEmpty(message = "账号不能为空")
    @Size(min = 3, max = 50)
    @Column(nullable = false,length = 20,unique = true)
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 3,max = 100)
    @Column(length = 100)
    private String password;

    @Column(length = 200)
    private String avatar;


    //用户和权限的关系
    @ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id" ),
    inverseJoinColumns = @JoinColumn(name = "authority_id",referencedColumnName = "id"))
    private List<Authority> authorities;

    protected User(){

    }

    public User(Long id,String name,String username,String email){
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        for (GrantedAuthority authority : this.authorities){
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }

        return simpleGrantedAuthorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    /**
     * 加密密码的方法
     */
    public void setEncodePassword(String password){
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePassword = encoder.encode(password);
        this.password = encodePassword;
    }

}

