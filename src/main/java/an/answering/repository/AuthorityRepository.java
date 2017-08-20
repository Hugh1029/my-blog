package an.answering.repository;

import an.answering.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限仓库
 * Created by HP on 2017/8/18.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
