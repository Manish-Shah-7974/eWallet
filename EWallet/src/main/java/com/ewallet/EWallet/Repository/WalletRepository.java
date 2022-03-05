package com.ewallet.EWallet.Repository;

import com.ewallet.EWallet.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {
    Wallet findByUserId(int userId);

}
