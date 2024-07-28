package com.javaapp.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import com.javaapp.exception.NotFoundException;

import io.opentelemetry.instrumentation.annotations.WithSpan;

import com.javaapp.Order;
import com.javaapp.OrderRepository;
import com.javaapp.Person;
import com.javaapp.PersonRepository;
import com.javaapp.exception.InvalidException;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrderService {

    @Inject
    OrderRepository orderRepository;

    @Inject
    PersonRepository personRepository;

    private static final Logger LOGGER = Logger.getLogger(OrderService.class);

  
    public List<Order> getAllOrders() {
        LOGGER.info("Retrieving all orders from repository");
        return orderRepository.listAll();
    }

    @WithSpan
    public Order getOrderById(Long id) {
        LOGGER.info("Retrieving order with ID" );
        Order order = orderRepository.findById(id);
        if (order == null) {
            LOGGER.warn("Order with ID " + id + " not found");
            throw new NotFoundException("Order not found");
        }
        return order;
    }

    @Transactional
    @WithSpan
    public Order createOrder(Order order) {
        validatePerson(order.person);
        LOGGER.info(" New order");
        orderRepository.persist(order);
        return order;
    }

    @Transactional
    @WithSpan
    public Order updateOrder(Long id, Order order) {
        LOGGER.info("Updating order with ID: " + id);
        Order existingOrder = orderRepository.findById(id);
        if (existingOrder == null) {
            LOGGER.warn("Order with ID " + id + " not found");
            throw new NotFoundException("Order not found");
        }
        validatePerson(order.person);
        existingOrder.productName = order.productName;
        existingOrder.quantity = order.quantity;
        existingOrder.person = order.person;
        LOGGER.info("Persisting updated order ");
        orderRepository.persist(existingOrder);
        return existingOrder;
    }

    @Transactional
    @WithSpan
    public void deleteOrder(Long id) {
        LOGGER.info("Deleting order ");
        Order order = orderRepository.findById(id);
        if (order == null) {
            LOGGER.warn("Order with ID " + id + " not found");
            throw new NotFoundException("Order not found");
        }
        orderRepository.delete(order);
    }

    private void validatePerson(Person person) {
        if (!personRepository.existsById(person.id)) {
            LOGGER.error("order with null or non-existent Person ID");
            throw new InvalidException("Person ID cannot be nil or non-existent");
        }
    }
}