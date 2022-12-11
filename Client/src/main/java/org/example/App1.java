package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class App1 {
    public static void main(String[] args) {

        String registryName = "Sensor from Client";
        registrySensor(registryName);

        int max = 90;
        for (int i = 0; i < 10; i++) {
            sendMeasurements(ThreadLocalRandom.current().nextBoolean(), ThreadLocalRandom.current().nextDouble() * max, registryName);
        }
    }

    public static void registrySensor(String name){
        String url = "http://localhost:8080/sensors/registration";
        HashMap<String,Object> jsonData = new HashMap<>();
        jsonData.put("name",name);
        makePostRequestWithJson(jsonData,url);
    }

    public static void sendMeasurements(Boolean raining,double value,String sensorName){
        String url = "http://localhost:8080/measurements/add";
        HashMap<String,Object> jsonData = new HashMap<>();
        jsonData.put("value",value);
        jsonData.put("raining",raining);
        jsonData.put("sensor", Map.of("name",sensorName));
        makePostRequestWithJson(jsonData,url);
    }

    public static void makePostRequestWithJson(HashMap<String, Object> jsonData, String url){
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap<String,Object>> httpEntity = new HttpEntity<>(jsonData,headers);

        try {
            restTemplate.postForObject(url, httpEntity, String.class);
        }
        catch (HttpClientErrorException e){
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }
}
