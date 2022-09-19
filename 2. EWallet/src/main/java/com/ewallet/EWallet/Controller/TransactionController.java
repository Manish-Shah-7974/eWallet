package com.ewallet.EWallet.Controller;

import com.ewallet.EWallet.Model.AddBalanceDetails;
import com.ewallet.EWallet.Model.Transaction;
import com.ewallet.EWallet.Model.User;
import com.ewallet.EWallet.Model.Wallet;
import com.ewallet.EWallet.Repository.TransactionRepository;
import com.ewallet.EWallet.Repository.WalletRepository;
import com.ewallet.EWallet.ThirdPartyServices.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@Transactional                           // To Rollback the transaction if Runtime Exception occurs like SQLException
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired                           // Using another Microservice(User) to validate if user exists or not 
    private UserService userService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping("/sendMoney")
    public Transaction sendMoney(@RequestBody Transaction transaction) throws Exception {

        /* From Request Transaction Body , we get sender ID and receiver ID */
        User sender = userService.findUserById(transaction.getSender_id());
        User receiver = userService.findUserById(transaction.getReceiver_id());

        if (sender == null || receiver == null) {
            logger.info("Either RECEIVER OR SENDER Is Null {},{}", sender.toString(), receiver.toString());
            throw new Exception("Bad PAYLOAD");
        }

        Wallet senderWallet = walletRepository.findByUserId(sender.getId());
        Wallet receiverWallet = walletRepository.findByUserId(receiver.getId());

        int amount = transaction.getAmount();
        if (senderWallet.getBalance() < amount) {
            logger.info("Current Balance {} is less than Required Balance i.e {}", senderWallet.getBalance(), amount);
            throw new Exception("INSUFFICIENT BALANCE ");
        }

        senderWallet.setBalance(senderWallet.getBalance() - amount);
        receiverWallet.setBalance(receiverWallet.getBalance() + amount);
        transaction.setStatus(1);                         // 1 - SuccessFul 0 - UnSuccessFul
        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);           
        
        logger.info("Transaction Was Successful ");
        logger.info("Amount deducted from sender " + sender.getMobile() + "is " + amount);
        logger.info("Amount credited to receiver " + receiver.getMobile() + "is " + amount);

        // Email Service linked to Apache Kafka to Send Notification
        try {
            userService.sendEmail("Amount deducted from sender "+sender.getMobile()+" is "+amount);
            userService.sendEmail("Amount credited to receiver "+receiver.getMobile()+" is "+amount);
        } catch (Exception exception) {
            logger.info("Email Service Is Down {}", exception.getStackTrace());
        }

        return transactionRepository.save(transaction);         // Transaction Completes here 
    }

    // addBalance
    // request body ---- { user id, amount }
    // Authentication Required to access this API by Admin only to add balance to users 
    
    @PutMapping("/addBalance")              
    public Integer addBalance(@RequestBody AddBalanceDetails addBalanceDetails) {
        logger.info("Got this request for adding balance : {}", addBalanceDetails.toString());
        // find wallet by user id
        Wallet wallet = walletRepository.findByUserId(addBalanceDetails.getUserid());
        // set balance in the wallet
        wallet.setBalance(addBalanceDetails.getAmount() + wallet.getBalance());
        walletRepository.save(wallet);
        return wallet.getBalance();
    }

    
    @GetMapping("/getBalance/{userid}")
    public Integer getBalance(@PathVariable int userid) throws Exception {
        Wallet wallet = walletRepository.findByUserId(userid);
        if (wallet == null) {
            throw new Exception("Wallet is not created for this user : " + userid);
        }
        return wallet.getBalance();
    }

}
