package io.dongvelop.orderservice.jpa;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 이동엽(Lee Dongyeop)
 * @date 2024. 06. 27
 * @description
 */
@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120, unique = true)
    private String productId;
    @Column(nullable = false)
    private Integer qty;
    @Column(nullable = false)
    private Integer unitPrice;
    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;
}
