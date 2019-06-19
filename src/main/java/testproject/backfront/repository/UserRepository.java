package testproject.backfront.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import testproject.backfront.entity.User;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    @Query("select user.id from User user")
    List<Long> getAllContactsById();

    User findUserByFullName(String userFullName);
}
