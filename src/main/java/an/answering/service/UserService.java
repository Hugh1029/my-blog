package an.answering.service;

import an.answering.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by HP on 2017/8/18.
 */
public interface UserService {

    /**
     * 新增、保持用户
     * @param user
     * @return
     */
    User saveOrUpdateUser(User user);


    /**
     * 注册用户
     * @param user
     * @return
     */
    User registerUser(User user);


    /**
     * 删除用户
     * @param id
     */
    void removeUser(Long id);


    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    User getUserById(Long id);


    Page<User> listUserByNameLise(String name, Pageable pageable);


}
