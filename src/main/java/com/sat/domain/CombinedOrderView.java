package com.sat.domain;

import lombok.Data;

import static com.sat.domain.OrderMatchingStatus.*;

@Data
public class CombinedOrderView {
    private final Order source1Order;
    private final Order source2Order;
    private final OrderMatchingStatus matchStatus;

    public static CombinedOrderView from(Order source1Order, Order source2Order) {
        OrderMatchingStatus viewType = null;
        if (source1Order != null && source2Order != null) {
            viewType = MATCHED;
        } else if (source1Order == null && source2Order != null) {
            viewType = MISSING_FROM_SOURCE1;
        } else if (source1Order != null && source2Order == null) {
            viewType = MISSING_FROM_SOURCE2;
        } else {
            throw new IllegalArgumentException();
        }

        return new CombinedOrderView(source1Order, source2Order, viewType);
    }
}
