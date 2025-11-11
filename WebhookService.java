package Jar.service.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

// import the GenerateResponse from its package (dto/dto)
import Jar.dto.dto.GenerateResponse;

@Service
public class WebhookService {

    private final RestTemplate rest = new RestTemplate();

    // <-- change these to your real details
    private final String name = "Asma Begum";
    private final String regNo = "PES2UG22CS109";
    private final String email = "your_email@example.com";

    private final String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
    private final String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";

    public void performFlow() {
        try {
            System.out.println("Generating webhook...");
            GenerateResponse gen = generateWebhook();
            System.out.println("Webhook: " + gen.getWebhook());
            System.out.println("AccessToken: " + gen.getAccessToken());

            String finalQuery = "SELECT P.AMOUNT AS SALARY, "
                    + "CONCAT(E.FIRST_NAME, ' ', E.LAST_NAME) AS NAME, "
                    + "TIMESTAMPDIFF(YEAR, E.DOB, CURDATE()) AS AGE, "
                    + "D.DEPARTMENT_NAME "
                    + "FROM PAYMENTS P "
                    + "JOIN EMPLOYEE E ON P.EMP_ID = E.EMP_ID "
                    + "JOIN DEPARTMENT D ON E.DEPARTMENT = D.DEPARTMENT_ID "
                    + "WHERE DAY(P.PAYMENT_TIME) <> 1 "
                    + "ORDER BY P.AMOUNT DESC "
                    + "LIMIT 1;";

            System.out.println("Submitting SQL...");
            submitFinalQuery(gen.getAccessToken(), finalQuery);
            System.out.println("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GenerateResponse generateWebhook() {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("regNo", regNo);
        body.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> req = new HttpEntity<>(body, headers);

        ResponseEntity<GenerateResponse> resp = rest.postForEntity(generateUrl, req, GenerateResponse.class);
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            return resp.getBody();
        } else {
            throw new RuntimeException("generateWebhook failed: " + resp.getStatusCode() + " body=" + resp.getBody());
        }
    }

    private void submitFinalQuery(String accessToken, String finalQuery) {
        Map<String, String> body = new HashMap<>();
        body.put("finalQuery", finalQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Map<String, String>> req = new HttpEntity<>(body, headers);
        ResponseEntity<String> resp = rest.postForEntity(submitUrl, req, String.class);

        System.out.println("Submit response: " + resp.getStatusCode());
        System.out.println("Body: " + resp.getBody());
    }
}
