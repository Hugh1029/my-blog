package an.answering.service.impl;

import an.answering.domain.Blog;
import an.answering.domain.User;
import an.answering.repository.BlogRepository;
import an.answering.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by HP on 2017/8/19.
 */
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        Blog returnBlog = blogRepository.save(blog);
        return returnBlog;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.delete(id);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findOne(id);
    }

    @Override
    public Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable) {
        title = "%" + title + "title";
        String tages = title;
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title,user,tages,pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogByTitleVoteAndSort(User user, String title, Pageable pageable) {
        title = "%" + title + "%";

        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user,title,pageable);
        return  blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findOne(id);
        blog.setReading(blog.getReading() + 1);
        this.saveBlog(blog);
    }
}
