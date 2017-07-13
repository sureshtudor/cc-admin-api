package com.cc.api.rest;

import com.cc.api.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(path = "/user-search-service")
public interface UserPagedRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE CONCAT('%', LOWER(:val), '%')")
    List<User> findByUsername(@Param("val") String val);

    @Query("SELECT u FROM User u WHERE LOWER(u.firstname) LIKE CONCAT('%', LOWER(:val), '%')")
    List<User> findByFirstname(@Param("val") String val);

    @Query("SELECT u FROM User u WHERE LOWER(u.lastname) LIKE CONCAT('%', LOWER(:val), '%')")
    List<User> findByLastname(@Param("val") String val);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) LIKE CONCAT('%', LOWER(:val), '%')")
    List<User> findByEmail(@Param("val") String val);
}