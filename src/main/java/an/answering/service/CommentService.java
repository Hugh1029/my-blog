package an.answering.service;

import an.answering.domain.Comment;

/**
 * Created by HP on 2017/8/20.
 */
public interface CommentService {

    Comment getCommentById(Long id);

    void removeComment(Long id);
}
