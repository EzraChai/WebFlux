package com.stpd.springboot.webflux.repository;

import com.stpd.springboot.webflux.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepo extends ReactiveMongoRepository<User,String> {
    /**
     * Find the Users according to the age.
     * @param start
     * @param end
     * @return
     */
    Flux<User> findByAgeBetween(int start, int end);

    @Query("{'age':{'$gte' : 20 , '$lte' : 30}}")
    Flux<User> findOldUser();
}
