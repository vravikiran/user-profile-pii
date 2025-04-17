package com.encrypt.decrypt.kms.pii.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.encrypt.decrypt.kms.pii.entities.KycDetails;

public interface KycDetailsRepository extends JpaRepository<KycDetails, Integer>{

}
