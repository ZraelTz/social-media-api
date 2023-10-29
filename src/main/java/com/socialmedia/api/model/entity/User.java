package com.socialmedia.api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User extends BaseEntity<Long> implements UserDetails {

    @Column
    private String profilePicUrl;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable
    @Builder.Default
    @JsonIgnore
    private Set<User> followers = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "followers")
    @Builder.Default
    @JsonIgnore
    private Set<User> following = new LinkedHashSet<>();

    @Builder.Default
    @Column(name = "follower_count")
    private int followerCount = 0;

    @Builder.Default
    @Column(name = "following_count")
    private int followingCount = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    @JsonIgnore
    private Set<Post> posts = new LinkedHashSet<>();

    public boolean addPost(Post post) {
        if (Objects.nonNull(post)) {
            return this.posts.add(post);
        }

        return false;
    }

    public boolean addFollowing(User user) {
        if (Objects.nonNull(user)) {
            boolean followingAdded = this.following.add(user);
            if (followingAdded) {
                followingCount += 1;

            }

            return followingAdded;
        }

        return false;
    }

    public void addFollower(User user) {
        if (Objects.nonNull(user)) {
            boolean followerAdded = this.followers.add(user);
            if (followerAdded) {
                followerCount += 1;
            }
        }
    }

    public boolean removeFollowing(User user) {
        if (Objects.nonNull(user)) {
            boolean followingRemoved = this.following.remove(user);
            if (followingRemoved) {
                followingCount -= 1;
            }

            return followingRemoved;
        }

        return false;
    }

    public boolean removeFollower(User user) {
        if (Objects.nonNull(user)) {
            boolean followerRemoved = this.followers.remove(user);
            if (followerRemoved) {
                followerCount -= 1;
            }
            return followerRemoved;
        }

        return false;
    }

    public boolean removePost(Post post) {
        if (Objects.nonNull(post)) {
            return this.posts.remove(post);
        }

        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
