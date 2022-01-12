package com.example.postuser.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Objects;

@Component
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String url;

    @ManyToOne
    @JoinColumn(name="post_id")
    @ToString.Exclude
    @JsonBackReference
private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Image image = (Image) o;
        return id != null && Objects.equals(id, image.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
