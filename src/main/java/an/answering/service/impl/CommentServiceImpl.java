package an.answering.service.impl;

import an.answering.domain.Comment;
import an.answering.repository.CommentRepository;
import an.answering.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by HP on 2017/8/20.
 */
@Service
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findOne(id);
    }

    @Transactional
    @Override
    public void removeComment(Long id) {
        commentRepository.delete(id);
    }
}
