package io.dongvelop.catalogservice.controller;

import io.dongvelop.catalogservice.jpa.CatalogEntity;
import io.dongvelop.catalogservice.service.CatalogService;
import io.dongvelop.catalogservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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
    private final DiscoveryClient discoveryClient;

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getUsers() {
        Iterable<CatalogEntity> userList = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        userList.forEach(userEntity -> result.add(new ModelMapper().map(userEntity, ResponseCatalog.class)));

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/health_check")
    public String status() {
        final List<ServiceInstance> serviceList = getApplications();
        for (ServiceInstance instance : serviceList) {
            System.out.println(String.format("instanceId:$s, serviceId:%s, host:%s, schema:%s, uri:%s",
                    instance.getInstanceId(), instance.getServiceId(), instance.getHost(), instance.getUri()));
        }


        return String.format("It's Working in User Service on PORT %s",
                env.getProperty("local.server.port"));
    }

    private List<ServiceInstance> getApplications() {

        final List<String> services = this.discoveryClient.getServices();
        final List<ServiceInstance> instances = new ArrayList<>();

        services.forEach(serviceName -> instances.addAll(this.discoveryClient.getInstances(serviceName)));

        return instances;
    }
}
