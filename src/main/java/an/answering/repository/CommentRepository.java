package an.answering.repository;

import an.answering.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by HP on 2017/8/20.
 */
public interface CommentRepository extends JpaRepository<Comment,Long>{

}
