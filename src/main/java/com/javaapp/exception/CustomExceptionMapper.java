package com.javaapp.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        if (exception instanceof NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
        } else if (exception instanceof DuplicateException) {
            return Response.status(Response.Status.CONFLICT).entity(exception.getMessage()).build();
        } else if (exception instanceof InvalidException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }
    
}
