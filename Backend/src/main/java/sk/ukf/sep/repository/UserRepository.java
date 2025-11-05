package sk.ukf.sep.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sk.ukf.sep.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
