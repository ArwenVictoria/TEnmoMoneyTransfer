package com.techelevator.tenmo.services;

import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.Account;

import com.techelevator.tenmo.models.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    public Account getAccountByUserId(long id, String token){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity request = new HttpEntity<>(headers);

            return restTemplate.exchange(BASE_URL + id +"/accounts/", HttpMethod.GET, request, Account.class).getBody();
        }
        catch(RestClientResponseException e){
            System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        }
        catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public User getUserByUserName(String name, String token){
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity request = new HttpEntity<>(headers);

            return restTemplate.exchange(BASE_URL + "users/"+name, HttpMethod.GET, request, User.class).getBody();
        }
        catch(RestClientResponseException e){
            System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        }
        catch (ResourceAccessException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Transfer[] listTransfers(long id, String token) {
        Transfer[] transfers = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity entity = new HttpEntity<>(headers);

          transfers = restTemplate.exchange(BASE_URL + id +"/transfers/", HttpMethod.GET, entity, Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
        	System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
        	System.out.println(e.getMessage());
        }
        return transfers;
      }

    public User[] listUsers(String token) {
        User[] users = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity entity = new HttpEntity<>(headers);

            users = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, entity, User[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    public Transfer[] getPendingTransfers(long id, String token){
        Transfer[] transfers = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity entity = new HttpEntity<>(headers);

            transfers = restTemplate.exchange(BASE_URL + id +"/transfers/pending", HttpMethod.GET, entity, Transfer[].class).getBody();
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transfers;
    }

    public void updateTransferStatus(Transfer transfer, String token){
        if (transfer == null) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        try {
            restTemplate.put(BASE_URL + "/transfers" , entity);
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public Account updateAccount(Account account, String token) {
        if (account == null) {
          return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
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

      public Transfer createTransfer(Transfer transfer, String token){
          if (transfer == null) {
              return null;
          }

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.APPLICATION_JSON);
          headers.setBearerAuth(token);
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

