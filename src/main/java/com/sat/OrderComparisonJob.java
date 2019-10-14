package com.sat;

import com.sat.domain.CombinedOrderView;
import com.sat.domain.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.JoinOperator;

import java.util.Arrays;
import java.util.List;

import static com.sat.domain.OrderMatchingStatus.*;

public class OrderComparisonJob {
    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        List<Order> orderFromSource1 = Arrays.asList(
                new Order("1"),
                new Order("10"),
                new Order("2"),
                new Order("3")
        );

        List<Order> orderFromSource2 = Arrays.asList(
                new Order("1"),
                new Order("10"),
                new Order("4"),
                new Order("5")
        );

        DataSet<Order> source1DataSet = env.fromCollection(orderFromSource1);
        DataSet<Order> source2DataSet = env.fromCollection(orderFromSource2);

        JoinOperator<Order, Order, CombinedOrderView> joinedView = source1DataSet.fullOuterJoin(source2DataSet)
                .where(Order::getId)
                .equalTo(Order::getId)
                .with(CombinedOrderView::from);

        System.out.println("Missing from source1");
        joinedView
                .filter(combinedOrderView -> combinedOrderView.getMatchStatus() == MISSING_FROM_SOURCE1)
                .print();

        System.out.println("Missing from source2");
        joinedView.filter(combinedOrderView -> combinedOrderView.getMatchStatus() == MISSING_FROM_SOURCE2)
                .print();

        System.out.println("Matched");
        joinedView.filter(combinedOrderView -> combinedOrderView.getMatchStatus() == MATCHED)
                .print();
    }
}
