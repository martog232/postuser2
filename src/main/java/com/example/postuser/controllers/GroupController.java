package com.example.postuser.controllers;


import com.example.postuser.controllers.config.ControllerConfig;
import com.example.postuser.model.dto.group.GroupDTO;
import com.example.postuser.services.group.GroupService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(ControllerConfig.GROUPS_URL)
public class GroupController {
    private final GroupService groupService;
    private final SessionManager sessionManager;

    @GetMapping()
    public List<GroupDTO> getAllGroups(HttpSession ses, @RequestParam(required = false) Integer loggedUserId) throws AuthenticationException {
        if (loggedUserId == null) return groupService.getAllGroups(sessionManager.getLoggedUser(ses));
        else return groupService.getAllGroups(loggedUserId);
    }

    @GetMapping("/not-joined")
    public List<GroupDTO> getAllNotJoinedGroups(HttpSession ses, @RequestParam(required = false) Integer loggedUserId) throws AuthenticationException {
        if (loggedUserId == null) return groupService.getAllNotJoinedGroups(sessionManager.getLoggedUser(ses));
        else return groupService.getAllNotJoinedGroups(loggedUserId);
    }

    @GetMapping("/joined")
    public List<GroupDTO> getAllJoinedGroups(HttpSession ses, @RequestParam(required = false) Integer loggedUserId) throws AuthenticationException {
        if (loggedUserId == null) return groupService.getAllJoinedGroups(sessionManager.getLoggedUser(ses));
        else return groupService.getAllJoinedGroups(loggedUserId);
    }

    @PostMapping(value = "/create-group")
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDTO createGroup(@RequestParam String name, @RequestParam String description, HttpSession ses, @RequestParam(required = false) Integer loggedUserId) throws AuthenticationException {
        if (loggedUserId == null) return groupService.create(name,description, sessionManager.getLoggedUser(ses));
        else return groupService.create(name,description, loggedUserId);
    }

    @GetMapping(value = "/{name}/search")
    public List<Object> findGroupByName(@PathVariable String name, HttpSession ses) throws AuthenticationException {

        return groupService.findByName(name, sessionManager.getLoggedUser(ses));
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<?> findGroupById(@PathVariable Integer groupId, HttpSession ses, @RequestParam(required = false) Integer loggedUserId) throws AuthenticationException {
        if (loggedUserId == null) return groupService.findById(groupId, sessionManager.getLoggedUser(ses));
        else return groupService.findById(groupId, loggedUserId);
    }

    @PostMapping(value = "/{id}/join")
    public ResponseEntity<?> joinAndLeave(@PathVariable Integer id, HttpSession ses, @RequestParam(required = false) Integer loggedUserId) throws AuthenticationException {
        if (loggedUserId == null) return groupService.joinAndLeave(id, sessionManager.getLoggedUser(ses));
        else return groupService.joinAndLeave(id, loggedUserId);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Integer id, HttpSession ses) throws AuthenticationException, AccessException {
        return groupService.deleteGroup(id, sessionManager.getLoggedUser(ses));
    }

    @PostMapping(value = "/{groupId}/add-admin")
    public ResponseEntity<?> addGroupAdmin(@PathVariable Integer groupId, @RequestParam String newAdminUserName, HttpSession ses, @RequestParam(required = false) Integer loggedUserId) throws AuthenticationException {
        if (loggedUserId == null) return groupService.addGroupMember(groupId,newAdminUserName, sessionManager.getLoggedUser(ses));
        else return groupService.addGroupMember(groupId,newAdminUserName, loggedUserId);

    }

    @PutMapping(value = "/{groupId}")
    public ResponseEntity<?> editGroup(@PathVariable Integer groupId, @RequestParam String name, @RequestParam String description, HttpSession ses, @RequestParam(required = false) Integer loggedUserId) throws AuthenticationException {
        if (loggedUserId == null) return groupService.editGroup(groupId, name, description, sessionManager.getLoggedUser(ses));
        else return groupService.editGroup(groupId, name, description, loggedUserId);
    }
}
