package com.encrypt.decrypt.kms.pii.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.encrypt.decrypt.kms.pii.entities.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
	@Query("select case when count(u) > 0 then true else false end from UserProfile u where u.email_hash= :email")
	public boolean existsByEmail(String email);

	@Query("select case when count(u) > 0 then true else false end from UserProfile u where u.panno_hash= :panno")
	public boolean existsByPanNumber(String panno);

	@Query(value = "select u from UserProfile u where u.email= :email")
	public Optional<UserProfile> findByEmail(String email);
}
