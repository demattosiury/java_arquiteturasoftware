package br.com.rufuziu.crud_users_and_auth.repository;

import br.com.rufuziu.crud_users_and_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail (String email);
    Boolean existsByEmailAndActiveFalse(String email);
    @Modifying
    @Query("UPDATE users u SET u.active = TRUE WHERE u.email = ?1")
    int activateUserByEmail(String email);
}
