package com.example.postuser.model.entities;

import com.example.postuser.model.dto.user.RegisterRequestUserDTO;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Post> posts;

    @ManyToMany(mappedBy = "followings", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<User> followers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_relations",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "following_id")})
    @ToString.Exclude
    private List<User> followings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_like_posts",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")})
    @ToString.Exclude
    private List<Post> likedPosts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_like_comments",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "comment_id")}
    )
    @ToString.Exclude
    private List<Comment> likedComments;

    @ManyToMany(mappedBy = "members")
    @ToString.Exclude
    private List<Group> groupMember;

    @ManyToMany(mappedBy = "admins")
    @ToString.Exclude
    private List<Group> groupAdmin;

    private boolean isConfirmed;


    public User(RegisterRequestUserDTO userDTO) {
        username = userDTO.getUsername();
        email = userDTO.getEmail();
        password = userDTO.getPassword();
        posts = new LinkedList<>();
        followers = new LinkedList<>();
        followings = new LinkedList<>() {
        };
        likedPosts = new LinkedList<>();
        likedComments = new LinkedList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
