package com.ewallet.EWallet.Repository;

import com.ewallet.EWallet.Model.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Integer> {

    UserAuth findByUsername(String userName);
}
