package io.dongvelop.catalogservice.service;

import io.dongvelop.catalogservice.jpa.CatalogEntity;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
