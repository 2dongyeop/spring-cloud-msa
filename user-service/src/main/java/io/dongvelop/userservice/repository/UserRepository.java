package io.dongvelop.userservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 22
 * @description
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
