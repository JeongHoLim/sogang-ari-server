package com.ari.sogang.domain.entity;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "USER")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "student_id") //email
    private String studentId;

    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserWishClub> userWishClubs;

    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserClub> userClubs;

    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,orphanRemoval = true)
    private Set<UserAuthority> authorities;

    @Override
    public String getUsername() {
        return studentId;
    }
    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

}
