package an.answering.repository;

import an.answering.domain.Blog;
import an.answering.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by HP on 2017/8/19.
 */
public interface BlogRepository extends JpaRepository<Blog,Long> {
    /**
     *
     * 根据用户名、博客标题分页查询博客列表
     */

    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    /**
     *
     * 根据用户名、博客查询博客列表（事件逆序）
     */
    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title,User user,String tage,Pageable pageable);
}
