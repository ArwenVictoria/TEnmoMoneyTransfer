package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Account;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.Console;

public class BankService {
    String BASE_URL;
    RestTemplate restTemplate;

    public BankService(String url){
        this.BASE_URL = url;
        restTemplate = new RestTemplate();
    }

    public Account getAccountById(long id){
        try {
            return restTemplate.getForObject(BASE_URL + id +"/accounts/", Account.class);
        }
        catch(RestClientResponseException e){
            System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        }
        catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }

        return null;
    }
}
