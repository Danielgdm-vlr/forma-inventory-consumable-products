package com.gdm.forma.inventory.consumableproducts.backend.model.entity;

import com.gdm.forma.inventory.consumableproducts.backend.model.util.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product")

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Product extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 123456L;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "stockInSalon")
    private Integer stockInSalon;

    @NonNull
    @Column(name = "stockPerWeek")
    private Integer stockPerWeek;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return getId() != null && Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}