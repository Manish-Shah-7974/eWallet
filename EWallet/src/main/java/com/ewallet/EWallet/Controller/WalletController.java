package com.ewallet.EWallet.Controller;
import com.ewallet.EWallet.Model.Wallet;
import com.ewallet.EWallet.Repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController
{
    @Autowired
    private WalletRepository walletRepository;
    @PostMapping("/create")
    public Integer createNewWallet(@RequestBody Wallet wallet)
    {
        Wallet walletObject = walletRepository.save(wallet);
       // walletRepository.change(walletObject.getId());
        return walletObject.getUserId();
    }
    @GetMapping("/findwallet/{userid}")
    public Wallet findWalletByUserId(@PathVariable int userid)
    {
        // Might Not Work ...
        return walletRepository.findByUserId(userid);
    }
    @PutMapping("/update")
    public Wallet updateWallet(@RequestBody Wallet wallet)
    {
        Wallet wallet1 =walletRepository.save(wallet);
        return wallet1;
    }



}
