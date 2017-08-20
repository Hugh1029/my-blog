package an.answering.service;

import an.answering.domain.Authority;

/**
 * Created by HP on 2017/8/18.
 */
public interface AuthorityService {

    /**
     * 根据id查询权限
     */

    Authority getAuthorityById(Long id);
}
