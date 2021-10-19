package com.example.postuser.model.entities;

import com.example.postuser.model.dto.RegisterRequestUserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Post> posts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="users_like_posts",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns ={ @JoinColumn(name="post_id")}
    )
   private List<Post> likedPosts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="users_like_comments",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns ={ @JoinColumn(name="comment_id")}
    )
   private List<Comment> likedComments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public User(RegisterRequestUserDTO userDTO){
        username = userDTO.getUsername();
        email=userDTO.getEmail();
        password=userDTO.getPassword();
        posts=new LinkedList<>();
        likedPosts= new LinkedList<>();
        likedComments=new LinkedList<>();
    }
}
