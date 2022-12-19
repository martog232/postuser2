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

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GroupDTO createGroup(@RequestParam String name, @RequestParam String description, HttpSession ses) throws AuthenticationException {
       return groupService.create(name,description, sessionManager.getLoggedUser(ses));

    }

    @GetMapping(value = "/{name}/search")
    public List<Object> findGroupByName(@PathVariable String name, HttpSession ses) throws AuthenticationException {
        return groupService.findByName(name, sessionManager.getLoggedUser(ses));
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<?> findGroupById(@PathVariable Integer groupId, HttpSession ses) throws AuthenticationException {
       return groupService.findById(groupId, sessionManager.getLoggedUser(ses));
    }

    @PostMapping(value = "/{id}/join")
    public ResponseEntity<?> joinAndLeave(@PathVariable Integer id, HttpSession ses) throws AuthenticationException {
        return groupService.joinAndLeave(id, sessionManager.getLoggedUser(ses));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Integer id, HttpSession ses) throws AuthenticationException, AccessException {
        return groupService.deleteGroup(id, sessionManager.getLoggedUser(ses));
    }

    @PostMapping(value = "/{groupId}/add-admin")
    public ResponseEntity<?> addGroupAdmin(@PathVariable Integer groupId, @RequestParam String newAdminUserName, HttpSession ses) throws AuthenticationException {
      return groupService.addGroupMember(groupId,newAdminUserName, sessionManager.getLoggedUser(ses));
    }

    @PutMapping(value = "/{groupId}")
    public ResponseEntity<?> editGroup(@PathVariable Integer groupId, @RequestParam String name, @RequestParam String description, HttpSession ses) throws AuthenticationException {
        return groupService.editGroup(groupId, name, description, sessionManager.getLoggedUser(ses));
    }


}
