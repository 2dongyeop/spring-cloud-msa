package io.dongvelop.catalogservice.service;

import io.dongvelop.catalogservice.jpa.CatalogEntity;
import io.dongvelop.catalogservice.jpa.CatalogRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
