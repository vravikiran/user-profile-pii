package com.encrypt.decrypt.kms.pii.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.encrypt.decrypt.kms.pii.entities.BankAccountDetails;

public interface BankAccountDetailsRepository extends JpaRepository<BankAccountDetails, Integer> {

}
