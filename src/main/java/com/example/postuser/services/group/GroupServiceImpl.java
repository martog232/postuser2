package com.example.postuser.services.group;

import com.example.postuser.controllers.error.APIErrorCode;
import com.example.postuser.exceptions.EntityNotFoundException;
import com.example.postuser.model.dto.group.GroupDTO;
import com.example.postuser.model.dto.group.NotJoinedGroupDTO;
import com.example.postuser.model.dto.user.UserWithNameDTO;
import com.example.postuser.model.dto.user.UserWithoutPassDTO;
import com.example.postuser.model.entities.Group;
import com.example.postuser.model.entities.User;
import com.example.postuser.model.repositories.GroupRepository;
import com.example.postuser.services.user.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final UserService userService;
    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<GroupDTO> getAllGroups(int loggedUserId) {
        return groupRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<Object> findByName(String name, int loggedUserId) {
        List<GroupDTO> groupDTOS = groupRepository.findGroupsByName(name).stream().map(this::mapToDTO).collect(Collectors.toList());
        UserWithoutPassDTO user = userService.getUserWithoutPassDTOById(loggedUserId).get();
        List<Object> resultList = new ArrayList<>();
        List<GroupDTO> joinedGroups = user.getGroupMember();

        for (GroupDTO groupDTO : groupDTOS) {
            for (GroupDTO joinedGroupDTO : joinedGroups) {

                if (!joinedGroupDTO.getId().equals(groupDTO.getId())) {
                    NotJoinedGroupDTO notJoinedGroupDTO = mapToNotJoinedGroupDTO(groupDTO);
                    resultList.add(notJoinedGroupDTO);
                } else resultList.add(groupDTO);

            }
        }
        return resultList;
    }

    public ResponseEntity<?> findById(Integer id, int loggedUserId) {
        Optional<GroupDTO> groupDTO = groupRepository.findById(id).map(this::mapToDTO);
        if (groupDTO.isPresent()) {
            List<GroupDTO> joinedGroups = userService.getUserWithoutPassDTOById(loggedUserId).get().getGroupMember();
            for (GroupDTO dto : joinedGroups) {
                if (dto.getId().equals(groupDTO.get().getId())) {
                    return new ResponseEntity<>(groupDTO.get(), HttpStatus.OK);
                }
            }
            GroupDTO notJoinedGroup = groupDTO.get();
            notJoinedGroup.setPosts(new ArrayList<>());
            return new ResponseEntity<>(notJoinedGroup, HttpStatus.OK);
        }
        return new ResponseEntity<>("Group not found",HttpStatus.NOT_FOUND);
    }

    public Optional<GroupDTO> findById(Integer id) {
        return Optional.ofNullable(groupRepository.findById(id).map(this::mapToDTO).orElseThrow(() ->
                new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription())));
    }

    @Override
    public GroupDTO create(String name, String description, Integer userId) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(name);
        groupDTO.setDescription(description);
        groupDTO.setPosts(new ArrayList<>());

        List<UserWithNameDTO> admins = new ArrayList<>();
        List<UserWithNameDTO> members = new ArrayList<>();
        UserWithNameDTO loggedUserDto =
                userService.getUserWithNameDTOById(userId).get();

        admins.add(loggedUserDto);
        members.add(loggedUserDto);
        groupDTO.setAdmins(admins);
        groupDTO.setMembers(members);
        Group group = mapToEntity(groupDTO);
        group = groupRepository.save(group);

        return findById(group.getId()).get();
    }

    public ResponseEntity<?> joinAndLeave(Integer groupId, Integer loggedUserId) {
        Optional<Group> optionalGroupDTO = groupRepository.findById(groupId);
        if (optionalGroupDTO.isPresent()) {
            Group group = optionalGroupDTO.get();
            List<User> members = group.getMembers();
            List<User> admins = group.getAdmins();
            User user = userService.getUserById(loggedUserId).get();
            if (members.contains(user)) {
                if (admins.contains(user)) {
                    if (admins.size() == 1)
                        return new ResponseEntity<>("first add admin", HttpStatus.METHOD_NOT_ALLOWED);
                    else {
                        admins.remove(user);
                        group.setAdmins(admins);
                    }
                }
                members.remove(user);
            } else members.add(user);
            group.setMembers(members);
            groupRepository.save(group);
            return new ResponseEntity<>(findById(groupId, loggedUserId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> deleteGroup(Integer groupId, Integer loggedUserId) {
        GroupDTO dto = mapToDTO(groupRepository.findById(groupId).get());
        List<UserWithNameDTO> admins = dto.getAdmins();
        for (UserWithNameDTO u : admins) {
            if (Objects.equals(u.getId(), loggedUserId)) {
                groupRepository.deleteGroupById(groupId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("you cant delete group, that not belongs to you", HttpStatus.METHOD_NOT_ALLOWED);
    }


    @Override
    public Group mapToEntity(GroupDTO groupDTO) {
        return modelMapper.map(groupDTO, Group.class);
    }

    @Override
    public GroupDTO mapToDTO(Group group) {
        return modelMapper.map(group, GroupDTO.class);
    }

    @Override
    public NotJoinedGroupDTO mapToNotJoinedGroupDTO(Group group) {
        return modelMapper.map(group, NotJoinedGroupDTO.class);
    }

    @Override
    public NotJoinedGroupDTO mapToNotJoinedGroupDTO(GroupDTO groupDTO) {
        return modelMapper.map(groupDTO, NotJoinedGroupDTO.class);
    }

    @Override
    public ResponseEntity<?> editGroup(Integer groupId, String name, String description, int loggedUserId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            List<User> admins = group.getAdmins();
            boolean isAdmin = false;
            for (User admin : admins) {
                if (admin.getId().equals(loggedUserId)) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) return new ResponseEntity<>("you are not member in group " + group.getName() +
                    " ! First join it", HttpStatus.METHOD_NOT_ALLOWED);
            if (name != null) group.setName(name);
            if (description != null) group.setDescription(description);
            groupRepository.save(group);
            return new ResponseEntity<>(findById(groupId, loggedUserId), HttpStatus.OK);

        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());

    }

    @Override
    @Transactional
    public ResponseEntity<?> addGroupMember(Integer groupId, String newAdminUsername, int loggedUser) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        Optional<User> newOptionalAdmin = userService.getUserByUserName(newAdminUsername);
        if (optionalGroup.isPresent()) {
            Group group = optionalGroup.get();
            List<User> admins = group.getAdmins();
            boolean isAdmin = false;
            for (User admin : admins) {
                if (admin.getId().equals(loggedUser)) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) return new ResponseEntity<>("you are not member in group " + group.getName() +
                    " ! First join it", HttpStatus.METHOD_NOT_ALLOWED);
            if (newOptionalAdmin.isPresent()) {
                if(admins.contains(newOptionalAdmin.get())) admins.remove(newOptionalAdmin.get());
                else admins.add(newOptionalAdmin.get());
                group.setAdmins(admins);
                groupRepository.saveAndFlush(group);
                return new ResponseEntity<>(findById(groupId).get(), HttpStatus.OK);
            } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());

        } else throw new EntityNotFoundException(APIErrorCode.ENTITY_NOT_FOUND.getDescription());
    }
}
