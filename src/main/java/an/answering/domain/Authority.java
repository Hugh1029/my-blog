package an.answering.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Created by HP on 2017/8/18.
 */
@Entity
public class Authority implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


}
