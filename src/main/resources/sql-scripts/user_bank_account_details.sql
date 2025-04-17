-- user_profile_mgmt_db.user_bank_account_details definition

CREATE TABLE `user_bank_account_details` (
  `bank_acc_details_id` int NOT NULL AUTO_INCREMENT,
  `account_holder_name` varchar(255) NOT NULL,
  `account_number` varchar(255) NOT NULL,
  `ifsc_code` varchar(255) NOT NULL,
  `created_date` date NOT NULL,
  `updated_date` date NOT NULL,
  PRIMARY KEY (`bank_acc_details_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;