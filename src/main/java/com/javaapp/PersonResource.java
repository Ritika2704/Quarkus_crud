package com.javaapp;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import com.javaapp.Service.PersonService;
import org.jboss.logging.Logger;



@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

  public static final Logger logger = Logger.getLogger(PersonResource.class);

  @Inject
  PersonService personService;

  @GET
  public List<Person> getAllPersons() {
      logger.info("GET request received for all persons");
      return personService.getAllPersonsFn();
  }

  @GET
  @Path("/{id}")
  public Response getPersonById(@PathParam("id") Long id) {
      logger.info("GET request received for person with ID");
      Person person = personService.getPersonByIdFn(id);
      return Response.ok(person).build();
  }

  @POST
  public Response createPerson(Person person) {
      logger.info("POST request received to create person");
      Person createdPerson = personService.createPersonFn(person);
      return Response.status(Response.Status.CREATED).entity(createdPerson).build();
  }

  @PUT
  @Path("/{id}")
  public Response updatePerson(@PathParam("id") Long id, Person person) {
      logger.info("PUT request received to update person with ID");
      Person updatedPerson = personService.updatePersonFn(id, person);
      return Response.ok(updatedPerson).build();
  }

  @DELETE
  @Path("/{id}")
  public Response deletePerson(@PathParam("id") Long id) {
      logger.info("DELETE request received for person with ID:" + id);
      personService.deletePersonFn(id);
      return Response.noContent().build();
  }
}