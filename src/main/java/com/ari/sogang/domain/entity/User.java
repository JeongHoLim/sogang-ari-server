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

    @Column(name = "student_id")
    private String studentId;

    private String password;

    private boolean enabled;

    private String major;

    private String email;


<<<<<<< HEAD
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<UserWishClub> userWishClubs;
=======
    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserWishList> userWishLists;
>>>>>>> 6304da71de26c6125c1b16c77f05e62fd35ef779

    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserClub> userClubs;

    @OneToMany(mappedBy = "userId",cascade = CascadeType.ALL,orphanRemoval = true)
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

    public void addUserClub(UserClub userClub) {
        this.userClubs.add(userClub);
    }
}
