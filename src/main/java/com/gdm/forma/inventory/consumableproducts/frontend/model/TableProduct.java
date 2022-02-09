package com.gdm.forma.inventory.consumableproducts.frontend.model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class TableProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    private @NonNull Long id;
    private @NonNull Label name;

    @EqualsAndHashCode.Include
    private @NonNull Integer stockInSalon;
    private @NonNull Integer stockToBuy;
    private @NonNull Integer stockPerWeek;

    private @NonNull Button minusStock;
    private @NonNull Button resetStock;
    private @NonNull Button editProduct;
    private @NonNull Button deleteProduct;

    public TableProduct(int stockInSalon){

        this.stockInSalon = stockInSalon;
    }
}