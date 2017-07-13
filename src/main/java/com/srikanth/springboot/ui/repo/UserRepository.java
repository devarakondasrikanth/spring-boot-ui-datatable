/**
 * 
 */
package com.srikanth.springboot.ui.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.srikanth.springboot.ui.domain.User;

/**
 * 
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value = "SELECT * FROM USER", nativeQuery = true)
	List<User> findAllByUsernames(List<String> listOfUsernames);
}
