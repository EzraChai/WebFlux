package com.stpd.springboot.webflux.controller;

import com.stpd.springboot.webflux.domain.User;
import com.stpd.springboot.webflux.repository.UserRepo;
import com.stpd.springboot.webflux.util.CheckUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Get all user data
     * @return
     */
    @GetMapping
    public Flux<User> getAll(){
        return this.userRepo.findAll();
    }

    /**
     * Get all user's data using SSE
     * @return
     */
    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getAllStream(){
        return this.userRepo.findAll();
    }
    /**
     * Add a new User
     * @param user
     * @return
     */
    @PostMapping("/")
    public Mono<User> createUser(@Valid @RequestBody User user){
        /**
         * In Spring data jpa, create and change is using save(),
         * if there is Id, it will change its data.
         */
        user.setId(null);
        user.setName(user.getName().trim());
        CheckUtil.checkName(user.getName());
        return this.userRepo.save(user);
    }

    /**
     * Delete a User
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") String id){
        /*
          If you want to operate the data and then return a MONO, you should use flatMap
          If you want not operating the data, just transform the data, you should use map
         */
        return this.userRepo.findById(id)
                .flatMap(user -> this.userRepo.delete(user)
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    /**
     * Update User's Info
     * If User exists, return 200, else return 404 Not Found
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>>  updateUser(@PathVariable("id") String id,@Valid  @RequestBody User user){
        return this.userRepo.findById(id)
                //flatMap -> Operate Data
                .flatMap(RetUser -> {
                    String userName = user.getName().trim();
                    CheckUtil.checkName(userName);
                    RetUser.setName(userName);
                    RetUser.setAge(user.getAge());
            return this.userRepo.save(RetUser);
        })
                //Map -> Transform Data
                .map(u -> new ResponseEntity<User>(u,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    /**
     * Find a User by Id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable("id") String id){
        return this.userRepo.findById(id).map((u) -> new ResponseEntity<User>(u,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    /**
     * Find Users according age
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/age/{start}/{end}")
    public Flux<User> findUserByAge(@PathVariable("start") int start, @PathVariable("end") int end){
        return this.userRepo.findByAgeBetween(start, end);
    }

    /**
     * Find Users according age
     * @param start
     * @param end
     * @return
     */
    @GetMapping(path = "/stream/age/{start}/{end}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindUserByAge(@PathVariable("start") int start, @PathVariable("end") int end){
        return this.userRepo.findByAgeBetween(start, end);
    }

    /**
     * Find Users with their age from 20 - 30
     * @return
     */
    @GetMapping("/age/old")
    public Flux<User> findUserByAgeFrom20To30(){
        return this.userRepo.findOldUser();
    }

    /**
     * Find Users with their age from 20 - 30
     * @return
     */
    @GetMapping(path = "/stream/age/old", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindUserByAgeFrom20To30(){
        return this.userRepo.findOldUser();
    }
}
