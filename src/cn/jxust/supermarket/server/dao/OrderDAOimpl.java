package cn.jxust.supermarket.server.dao;

import cn.jxust.supermarket.domain.Order;
import cn.jxust.supermarket.domain.OrderItem;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDAOimpl implements OrderDAO {
    private final Set<Order> orders = Collections.synchronizedSet(new LinkedHashSet<>());
    private final ProductDAO productDAO;

    public OrderDAOimpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public void addOrder(Order order) {
        orders.add(order);
    }

    @Override
    public void removeOrder(Order order) {
        orders.remove(order);
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) {
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getCreatTime().equals(date)) {
                filteredOrders.add(order);
            }
        }

        return filteredOrders;
    }


    @Override
    public List<Order> getOrdersByProduct(int productId) {
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order instanceof OrderItem orderItem) {
                if (orderItem.getProduct().getId() == productId) {
                    filteredOrders.add(order);
                }
            }
        }

        return filteredOrders;
    }


    @Override
    public double getTotalSalesByDate(LocalDate date) {
        List<Order> ordersByDate = getOrdersByDate(date);
        double totalSales = 0.0;

        for (Order order : ordersByDate) {
            if (order instanceof OrderItem) {
                OrderItem orderItem = (OrderItem) order;
                double orderSales = orderItem.getProduct().getPrice() * orderItem.getQuantity();
                totalSales += orderSales;
            }
        }

        return totalSales;
    }


    @Override
    public double getTotalSalesByProduct(int productId) {
        List<Order> ordersByProduct = getOrdersByProduct(productId);
        double totalSales = 0.0;

        for (Order order : ordersByProduct) {
            if (order instanceof OrderItem) {
                OrderItem orderItem = (OrderItem) order;
                double orderSales = orderItem.getProduct().getPrice() * orderItem.getQuantity();
                totalSales += orderSales;
            }
        }

        return totalSales;
    }


    @Override
    public double getTotalSales() {
        double totalSales = 0.0;

        for (Order order : orders) {
            if (order instanceof OrderItem) {
                OrderItem orderItem = (OrderItem) order;
                double orderSales = orderItem.getProduct().getPrice() * orderItem.getQuantity();
                totalSales += orderSales;
            }
        }

        return totalSales;
    }


    @Override
    public String getMostPopularProduct(LocalDate date) {
        Map<Integer, Integer> productCount = new HashMap<>();
        for (Order order : getOrdersByDate(date)) {
            OrderItem item = (OrderItem) order;
            productCount.put(item.getProduct().getId(), productCount.getOrDefault(item.getProduct().getId(), 0) + item.getQuantity());
        }
        Integer mostPopularProductId = productCount.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        if (mostPopularProductId != null) {
            return productDAO.getProductById(mostPopularProductId).getName();
        } else {
            return null;
        }
    }

    @Override
    public String getLeastPopularProduct(LocalDate date) {
        Map<Integer, Integer> productCount = new HashMap<>();
        for (Order order : getOrdersByDate(date)) {
            OrderItem item = (OrderItem) order;
            productCount.put(item.getProduct().getId(), productCount.getOrDefault(item.getProduct().getId(), 0) + item.getQuantity());
        }

        Integer mostPopularProductId = productCount.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        if (mostPopularProductId != null) {
            return productDAO.getProductById(mostPopularProductId).getName();
        } else {
            return null;
        }
    }
}
