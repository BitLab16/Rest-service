package site.bitlab16.restservice.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.bitlab16.restservice.model.Gathering;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class PredictionRepository {

    //private final WebClient client;
    private final RestTemplate restTemplate;

    private HttpEntity<Object> requestEntity;

    @Value(value = "${ml.apiAddress}")
    private String baseUrl;

    PredictionRepository() {
        //System.setProperty("java.net.preferIPv4Stack" , "true");
        //client = WebClient.builder().baseUrl("http//:127.0.0.1:3000").build();
        //client = WebClient.create(baseUrl);
        restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        this.requestEntity = new HttpEntity<Object>(requestHeaders);
    }

    public Gathering findAllFromInterval(LocalDateTime from, LocalDateTime to)
    {
        var uri = new DefaultUriBuilderFactory().builder()
                .path( baseUrl + "/predictions/")
                .queryParam("from", "{time_from}" )
                .queryParam("to", "{time_to}" )
                .build(from, to);
        System.out.println(restTemplate.getForObject(uri, String.class));
        return restTemplate.getForObject(uri, Gathering.class);
        /*
        var a = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path( "/predictions/")
                        //.port(6001)
                        .queryParam("from", "{time_from}" )
                        .queryParam("to", "{time_to}" )
                        .build(from, to));
        System.out.println(a);
        return a
                .retrieve()
                .bodyToFlux(Gathering.class);*/
    }

    public List<Gathering> findByIdFromInterval(Long pointId, LocalDateTime from, LocalDateTime to)
    {
        
        var uri = new DefaultUriBuilderFactory().builder()
                .path(baseUrl + "/prediction/" + pointId )
                .queryParam("from", "{time_from}" )
                .queryParam("to", "{time_to}" )
                .build(from.toString().replace('T', ' '), to.toString().replace('T', ' '));
        System.out.println(uri);
        System.out.println(restTemplate.getForObject(uri,  String.class));
        //var a = restTemplate.getForEntity(baseUrl + uri, Gathering[].class);
        return Collections.emptyList();
        /*
        var builder = new DefaultUriBuilderFactory().builder();
        var a = builder
                        .path( "/prediction/" + pointId)
                        //.port(6001)
                        .queryParam("from", "{time_from}" )
                        .queryParam("to", "{time_to}" )
                        .build(from.toString().replace('T', ' '), to.toString().replace('T', ' '));
        System.out.println(baseUrl);
        System.out.println(a);
        return client.get().uri(a)
                .retrieve()
                .bodyToFlux(Gathering.class);*/
    }


}
