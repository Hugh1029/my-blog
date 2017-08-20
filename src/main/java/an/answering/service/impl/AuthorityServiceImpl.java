package an.answering.service.impl;

import an.answering.domain.Authority;
import an.answering.repository.AuthorityRepository;
import an.answering.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by HP on 2017/8/18.
 */

@Service
public class AuthorityServiceImpl implements AuthorityService{

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }
}
