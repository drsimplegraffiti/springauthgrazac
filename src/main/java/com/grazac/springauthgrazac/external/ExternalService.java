package com.grazac.springauthgrazac.external;

import com.grazac.springauthgrazac.product.ProductDto;
import com.grazac.springauthgrazac.product.ProductDtoResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class ExternalService {
private final RestTemplate restTemplate;  // public PasswordEncoder

    public ExternalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createProduct(ExternalProductDro productDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer "+productDto.getToken());
        HttpEntity<ExternalProductDro> entity = new HttpEntity<ExternalProductDro>(productDto,headers);

        return restTemplate.exchange(
                "http://localhost:9090/api/v2/products/create", HttpMethod.POST, entity, String.class).getBody();

    }

//    eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwaW5reSIsInJvbGUiOiJbUk9MRV9VU0VSLCBGQUNUT1JfUEFTU1dPUkRdIiwidG9rZW5UeXBlIjoiYWNjZXNzIiwiaWF0IjoxNzc1MDM3MTcyLCJleHAiOjE3NzUwNDA3NzJ9.hPxbphCHiHcqUtbYWOirYPXX7XlMqMn1DxyUrRQcCBg

    public String getProductList() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("channel", "WEB");
        headers.add("x-api-key", "98765434567");
        headers.add("Authorization", "9876545678765");
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON)); // xml, text/html
        HttpEntity <String> entity = new HttpEntity<String>(headers);

//        String body = restTemplate.exchange("http://localhost:9090/customers/products/all", HttpMethod.GET, entity, String.class).getBody();
        String body = String.valueOf(restTemplate.exchange("http://localhost:9090/customers/products/all", HttpMethod.GET, entity, String.class).getHeaders());
        System.out.println(headers);
        return body;
    }
}
