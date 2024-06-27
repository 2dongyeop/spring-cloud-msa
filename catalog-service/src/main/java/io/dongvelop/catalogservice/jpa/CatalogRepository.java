package io.dongvelop.catalogservice.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@Repository
public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {

    CatalogEntity findByProductId(String productId);
}
