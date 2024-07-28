package com.javaapp;

import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.javaapp.Service.OrderService;
import java.util.List;
import org.jboss.logging.Logger;


@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService orderService;

    private static final Logger LOGGER = Logger.getLogger(OrderResource.class);

    @GET
    public List<Order> getAllOrders() {
        LOGGER.info("Fetching all orders");
        return orderService.getAllOrders();
    }

    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") Long id) {
        LOGGER.info("Fetching order with ID ");
        Order order = orderService.getOrderById(id);
        return Response.ok(order).build();
    }

    @POST
    public Response createOrder(Order order) {
        LOGGER.info("Creating new order");
        Order createdOrder = orderService.createOrder(order);
        return Response.status(Response.Status.CREATED).entity(createdOrder).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOrder(@PathParam("id") Long id, Order order) {
        LOGGER.debug("Updating order with ID ");
        Order updatedOrder = orderService.updateOrder(id, order);
        return Response.ok(updatedOrder).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") Long id) {
        LOGGER.info("Deleting order with ID" );
        orderService.deleteOrder(id);
        return Response.noContent().build();
    }
}