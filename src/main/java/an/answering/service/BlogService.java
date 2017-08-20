package an.answering.service;

import an.answering.domain.Blog;
import an.answering.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by HP on 2017/8/19.
 */
public interface BlogService {

    Blog saveBlog(Blog blog);

    void removeBlog(Long id);

    Blog getBlogById(Long id);

    Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable);

    Page<Blog> listBlogByTitleVoteAndSort(User user,String title,Pageable pageable);

    void readingIncrease(Long id);

    /**
     * 发表评论
     */
    Blog createComment(Long blogId,String commentContent);

    /**
     * 删除评论
     */
}
