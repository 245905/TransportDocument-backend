package com.tui.transport.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class SmsService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${smsapi.token}")
    private String apiToken;

    private static final String SEND_URL = "https://api.smsapi.pl/mfa/codes";
    private static final String VERIFY_URL = "https://api.smsapi.pl/mfa/codes/verifications";


    public Map<String, Object> sendCode(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Numer telefonu nie może być pusty");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        String jsonBody = String.format("""
        {
            "phone_number": "%s",
            "from": "Test",
            "content": "Twój kod logowania: [%%code%%]",
            "fast": "1"
        }
        """, phoneNumber);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    SEND_URL,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<>() {}
            );

            System.out.println("✅ Sukces! Response: " + response.getBody());
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("❌ Błąd HTTP!");
            System.out.println("Status: " + e.getStatusCode());
            System.out.println("Response body: " + e.getResponseBodyAsString());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Nie udało się wysłać kodu: " + e.getMessage());
        }
    }

    public boolean verifyCode(String phoneNumber, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);


        String jsonBody = String.format("""
        {
            "phone_number": "%s",
            "code": "%s"
        }
        """, phoneNumber, code);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity(VERIFY_URL, request, Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            return true; // 204 = poprawny kod
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Błędny kod");
        } else if (response.getStatusCodeValue() == 408) {
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Kod wygasł");
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Nieznany błąd weryfikacji kodu");
        }
    }

}
