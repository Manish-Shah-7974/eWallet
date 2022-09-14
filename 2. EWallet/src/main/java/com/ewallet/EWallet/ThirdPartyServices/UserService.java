package com.ewallet.EWallet.ThirdPartyServices;
import com.ewallet.EWallet.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService
{
    @Autowired
    private RestTemplate restTemplate;
    
   /* Connecting ewallet microservice to Apache Kakfa to send notification after transaction completion */ 
   
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private String kafkaTopic="CommunicationTopic";

    public  void sendEmail(String content) {
     kafkaTemplate.send(kafkaTopic,content);
    }

    /* Interaction with get API of another microservice i.e User to verify if sender & receiver both exists or not */
    
    public User findUserById(int userid){
       // String url = "http://localhost:8080/users/findUser/{id}";
         String url = "http://user-service/users/findUser/{id}";

        Map<String, Integer> params= new HashMap<String, Integer>();
        params.put("id", userid);
        try {
            ResponseEntity<User> responseEntity = restTemplate.getForEntity(url, User.class, params);
            if(responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                {

                    return null;
                }
            }else{
                return responseEntity.getBody();
            }
        }catch(Exception exception){
            System.out.println("Some Error while getting User ID from another microservice ");
            System.out.println(exception);
            return null;
        }
    }
}
