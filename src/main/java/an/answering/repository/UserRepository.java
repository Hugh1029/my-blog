package an.answering.repository;

import an.answering.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by HP on 2017/8/18.
 * JAP支持分页排序
 */
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * 根据用户姓名分页查询用户列表
     * @param name
     * @param pageable
     * @return
     */
    Page<User> findByUsernameLike(String name, Pageable pageable);

    /**
     * 根据账号查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);
}
