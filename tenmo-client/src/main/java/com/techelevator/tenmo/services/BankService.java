package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.Account;

import com.techelevator.tenmo.models.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class BankService {
	 private final String BASE_URL;
	  private RestTemplate restTemplate = new RestTemplate();
	

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
    
    public Transfer[] listTransfers(long id) {
        Transfer[] transfers = null;
        try {
          transfers = restTemplate.getForObject(BASE_URL + id +"/transfers/", Transfer[].class);
        } catch (RestClientResponseException e) {
        	System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
        	System.out.println(e.getMessage());
        }
        return transfers;
      }

    public User[] listUsers() {
        User[] users = null;
        try {
            users = restTemplate.getForObject(BASE_URL + "/users", User[].class);
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }
    
    public Transfer getTransfer(int transferId) {
        Transfer transfer = null;
        try {
          transfer = restTemplate.getForObject(BASE_URL + "/transfers/" + transferId, Transfer.class);
        } catch (RestClientResponseException e) {
        	System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
        	System.out.println(e.getMessage());
        }
        return transfer;
      }
    
    public Account updateAccount(Account account) {
        if (account == null) {
          return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);

        try {
          restTemplate.put(BASE_URL + "/accounts" , entity);
        } catch (RestClientResponseException e) {
          System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
          System.out.println(e.getMessage());
        }
        return account;
      }

      public Transfer createTransfer(Transfer transfer){
          if (transfer == null) {
              return null;
          }

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

          try {
              restTemplate.postForObject(BASE_URL + "/transfers" , entity, Transfer.class);
          } catch (RestClientResponseException e) {
              System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
          } catch (ResourceAccessException e) {
              System.out.println(e.getMessage());
          }
          return transfer;
      }

	
			
	}

