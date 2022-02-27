package com.ari.sogang.domain.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static java.lang.String.format;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebIntegrationTest {

    @LocalServerPort
    int port;

    public URI uri(String path) {
        try {
            return new URI(format("http://localhost:%d%s", port, path));
        }catch(Exception ex){
            throw new IllegalArgumentException();
        }
    }
    public URI uri(String path,String pathVariable){
        return UriComponentsBuilder
                .fromUriString(format("http://localhost:%d",port))
                .path(path)
                .encode()
                .build()
                .expand(pathVariable)
                .toUri();
    }

}