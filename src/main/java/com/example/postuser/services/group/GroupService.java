package com.example.postuser.services.group;

import com.example.postuser.model.dto.group.GroupDTO;
import com.example.postuser.model.dto.group.NotJoinedGroupDTO;
import com.example.postuser.model.dto.post.PostDTO;
import com.example.postuser.model.entities.Group;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<GroupDTO> getAllGroups(int loggedUserId);

    ResponseEntity<?> findById(Integer id,int loggedUserId);

    Optional<GroupDTO> findById(Integer id);

    List<Object> findByName(String name,int loggedUserId);

    GroupDTO create(String name, String description, Integer userId);

    ResponseEntity<?> joinAndLeave(Integer groupId,Integer loggedUserId);

    ResponseEntity<?> deleteGroup(Integer id, Integer loggedUser) throws AccessException;

    Group mapToEntity(GroupDTO groupDTO);

    GroupDTO mapToDTO(Group group);

    NotJoinedGroupDTO mapToNotJoinedGroupDTO(Group group);

    NotJoinedGroupDTO mapToNotJoinedGroupDTO(GroupDTO groupDTO);

//    GroupDTO createPost(String content, List<MultipartFile> photoList, Integer loggedUser, Integer groupId) throws IOException;
//
//    PostDTO setGroupToPost(Integer groupId, PostDTO postDTO);
}
