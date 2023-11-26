package cn.jxust.supermarket.server.service;

import cn.jxust.supermarket.server.dao.OrderDAO;
import cn.jxust.supermarket.server.dao.ProductDAO;
import cn.jxust.supermarket.server.dao.UserDAO;
import cn.jxust.supermarket.server.dao.exception.OutOfStockException;
import cn.jxust.supermarket.domain.*;
import cn.jxust.supermarket.domain.exception.FoodExpiredException;
import cn.jxust.supermarket.server.service.exception.PaymentException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class PaymentServiceImpl implements PaymentService {
    private final UserDAO userDAO;
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;

    public PaymentServiceImpl(UserDAO userDAO, ProductDAO productDAO, OrderDAO orderDAO) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
    }

    // Inner classes to hold intermediate calculation results and final payment info
    private record CalculateResult(boolean isVipUser, int productId, int quantity, int pointsCost,
                                   double priceBeforeDiscount, double priceAfterDiscount) implements Serializable{
    }

    public record PaymentInfo(Order[] orders, int earnPoints, double totalPayed) implements Serializable {

    }

    // Calculate price for a single product considering user type and remaining points
    private CalculateResult calculatePrice(int userId, int productId, int quantity, int remainingPoints) {
        Product product = productDAO.getProductById(productId);
        User user = userDAO.getUserById(userId);
        boolean isVipUser = user instanceof VipUser;
        int pointsCost = 0;
        double priceBeforeDiscount = product.getPriceBefore() * quantity;
        double discount = product.getDiscount();
        double priceAfterDiscount = priceBeforeDiscount * discount;

        if (isVipUser) {
            pointsCost = calculatePointsCost(remainingPoints, priceAfterDiscount);
            priceAfterDiscount -= pointsCost;
        }

        return new CalculateResult(isVipUser, productId, quantity, pointsCost, priceBeforeDiscount, priceAfterDiscount);
    }

    // Calculate points cost for VIP users
    private int calculatePointsCost(int remainingPoints, double priceAfterDiscount) {
        return remainingPoints >= priceAfterDiscount ? (int) priceAfterDiscount : remainingPoints;
    }

    // Check if any food product in the cart is expired
    private void checkFoodExpiry(Set<Integer> productIds) throws FoodExpiredException {
        for (Integer productId : productIds) {
            Product product = productDAO.getProductById(productId);
            if (product instanceof Food food && food.getExpiredDate().compareTo(LocalDate.now()) < 0) {
                throw new FoodExpiredException(food.getName() + " ID:" + food.getId(), food.getExpiredDate());
            }
        }
    }

    // Calculate the earned points based on the price before discount
    private static int getEarnPoints(double priceBeforeDiscount) {
        return (int) (priceBeforeDiscount * 0.1);
    }

    @Override
    public PaymentInfo payAllInCart(int userId) throws PaymentException, FoodExpiredException {
        User user = userDAO.getUserById(userId);
        Cart cart = user.getCart();
        Set<Integer> productIds = cart.getItemsMap().keySet();

        // Check for expired food items
        checkFoodExpiry(productIds);

        // Process payment for each product in the cart
        ArrayList<CalculateResult> calculateResults = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : user.getCart().getItemsMap().entrySet()) {
            Integer productId = entry.getKey();
            Integer quantity = entry.getValue();
            CalculateResult result = processPaymentForProduct(userId, productId, quantity);
            calculateResults.add(result);
        }

        // Create and save orders for all paid products
        PaymentInfo paymentInfo = createAndSaveOrders(user, calculateResults);

        // Clear the cart
        user.getCart().getItemsMap().clear();

        return paymentInfo;
    }

    private CalculateResult processPaymentForProduct(int userId, int productId, int quantity) throws PaymentException {
        User user = userDAO.getUserById(userId);
        boolean isVipUser = user instanceof VipUser;
        int remainingPoints = isVipUser ? ((VipUser) user).getVipPoints() : 0;

        CalculateResult calculateResult = calculatePrice(userId, productId, quantity, remainingPoints);

        double priceAfterDiscount = calculateResult.priceAfterDiscount;
        int pointsCost = calculateResult.pointsCost;

        if (user.getBalance() < priceAfterDiscount) {
            throw new PaymentException("Insufficient balance (" + user.getBalance() + ")!");
        } else {
            user.setBalance(user.getBalance() - priceAfterDiscount);
            if (isVipUser) {
                VipUser vipUser = (VipUser) user;
                vipUser.setVipPoints(vipUser.getVipPoints() - pointsCost + getEarnPoints(calculateResult.priceBeforeDiscount));
            }
            userDAO.updateUser(user);
        }

        productDAO.decreaseStock(productId, quantity);

        return calculateResult;
    }

    // Create and save orders to OrderDAO
    private PaymentInfo createAndSaveOrders(User user, ArrayList<CalculateResult> calculateResults) {
        double priceBeforeDiscountAll = 0.0;
        double priceAfterDiscountAll = 0.0;

        ArrayList<Order> orders = new ArrayList<>();
        for (CalculateResult result : calculateResults) {
            Product product = productDAO.getProductById(result.productId());
            int quantity = result.quantity();
            int pointsCost = result.pointsCost();
            double priceBeforeDiscount = result.priceBeforeDiscount();
            double priceAfterDiscount = result.priceAfterDiscount();

            priceBeforeDiscountAll += priceBeforeDiscount;
            priceAfterDiscountAll += priceAfterDiscount;

            boolean isVipUser = user instanceof VipUser;
            LocalDate currentDate = LocalDate.now();
            Order order = new OrderItem(isVipUser, product, currentDate, quantity, pointsCost, priceBeforeDiscount, priceAfterDiscount);
            orderDAO.addOrder(order);
            orders.add(order);
        }

        Order[] ordersArray = orders.toArray(new Order[0]);
        int earnedPoints = getEarnPoints(priceBeforeDiscountAll);

        return new PaymentInfo(ordersArray, earnedPoints, priceAfterDiscountAll);
    }


    @Override
    public PaymentInfo payForProduct(int userId, int productId, int quantity) throws PaymentException, OutOfStockException, FoodExpiredException {
// Check for expired food item
        checkFoodExpiry(Set.of(productId));
// Process payment for the product
        CalculateResult calculateResult = processPaymentForProduct(userId, productId, quantity);

// Create and save order for the paid product

        return createAndSaveOrders(userDAO.getUserById(userId), new ArrayList<>(List.of(calculateResult)));
    }

        @Override
    public void pay(int userId, double amount) throws PaymentException {

    }
}