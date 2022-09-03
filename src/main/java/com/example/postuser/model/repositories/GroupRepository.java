package com.example.postuser.model.repositories;

import com.example.postuser.model.dto.group.GroupDTO;
import com.example.postuser.model.entities.Group;
import com.example.postuser.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Transactional
    @Modifying
    @Query("delete from Group p where p.id = ?1")
    void deleteGroupById(Integer id);

    @Query("select g from Group g where g.name like CONCAT(?1,'%')")
    List<Group> findGroupsByName(String name);

    @Query("select g.admins from Group g where g.id =?1")
    List<User> getAdmins(Integer groupId);

}
