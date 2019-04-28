package com.project.Evenemenetyback.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.project.Evenemenetyback.domain.User;
import com.project.Evenemenetyback.message.response.ResponseMessage;
import com.project.Evenemenetyback.repository.UserRepository;
import com.project.Evenemenetyback.service.UserService;
import com.project.Evenemenetyback.service.dto.UserDTO;
import com.project.Evenemenetyback.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    private final UserRepository userRepository;

    public UserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Timed
    public List<User> getAllUsers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all users");
        return userService.findAll();
    }

    @DeleteMapping("/deleteUser/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Timed
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        log.debug("REST request to delete User: {}", username);

         userService.deleteByUsername(username);
         return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.deleted", username)).build();

    }

    @PutMapping("/updateUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Timed
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO){

        log.debug("REST request to update User : {}", userDTO);
        if ((userRepository.existsByUsername(userDTO.getUsername())) && !(userDTO.getUsername().equalsIgnoreCase(userRepository.findById(userDTO.getId()).get().getUsername())) ) {
            return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if ((userRepository.existsByEmail(userDTO.getEmail())) && !(userDTO.getEmail().equalsIgnoreCase(userRepository.findById(userDTO.getId()).get().getEmail())) ){
            return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

            User oldUser = userRepository.findById(userDTO.getId()).get();
            User updatedUser = userService.updateUser(userDTO);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("userManagement.updated", oldUser.getUsername())).build();

    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Timed
    public ResponseEntity<?> getUserDetails(@PathVariable String username){

        if(!userRepository.existsByUsername(username)){
            return new ResponseEntity<>(new ResponseMessage("Fail -> User not found"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByUsername(username).get();

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Listing user's details", user.getUsername())).body(user);

    }



}
