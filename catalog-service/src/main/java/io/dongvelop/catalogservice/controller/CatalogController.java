package io.dongvelop.catalogservice.controller;

import io.dongvelop.catalogservice.jpa.CatalogEntity;
import io.dongvelop.catalogservice.service.CatalogService;
import io.dongvelop.catalogservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@RestController
@RequestMapping("/catalog-service")
@RequiredArgsConstructor
public class CatalogController {

    private final Environment env;
    private final CatalogService catalogService;

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getUsers() {
        Iterable<CatalogEntity> userList = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        userList.forEach(userEntity -> result.add(new ModelMapper().map(userEntity, ResponseCatalog.class)));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service on PORT %s",
                env.getProperty("local.server.port"));
    }
}
