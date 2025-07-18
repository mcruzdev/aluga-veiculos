package tech.ada.resource;

import io.quarkus.panache.common.Parameters;
import jakarta.transaction.Transactional;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech.ada.dto.VehicleRequestBody;
import tech.ada.dto.VehicleResponseBody;
import tech.ada.model.Vehicle;

import java.net.URI;
import java.util.List;

@Path("/api/v1/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleResource {

    @POST
    @Transactional
    public Response create(
            VehicleRequestBody body
    ) {

        try {
            Vehicle vehicle = new Vehicle(
                    body.getModel(),
                    body.getYear(),
                    body.getEngine()
            );

            vehicle.persist();

            return Response.created(URI.create("/api/v1/vehicles/" + vehicle.getId())).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }


    }

    @GET
    public List<VehicleResponseBody> findAll() {
        List<Vehicle> vehicles = Vehicle.listAll();
        return vehicles.stream()
                .map(VehicleResponseBody::new).toList();
    }
//
//    @GET
//    @Path("{id}")
//    public Response findById(@PathParam("id") Long id) {
//
//        Vehicle vehicle = VEHICLES.get(id);
//
//        if (vehicle == null) {
//            return Response.status(404).build();
//        }
//
//        VehicleResponseBody vehicleResponseBody = new VehicleResponseBody(vehicle);
//
//        return Response.ok(
//                vehicleResponseBody
//        ).build();
//    }
//
//    @DELETE
//    @Path("{id}")
//    public Response delete(@PathParam("id") Long id) {
//        Vehicle vehicle = VEHICLES.get(id);
//        if (vehicle == null) {
//            return Response.status(404).build();
//        }
//
//        if (vehicle.isRented()) {
//            return Response.status(Response.Status.CONFLICT).build();
//        }
//
//        VEHICLES.remove(id);
//
//        return Response.noContent().build();
//    }
//
//    @PATCH
//    @Path("/{id}")
//    public Response changeVehicle(@PathParam("id") Long id, UpdateVehicleStatusRequestBody body) {
//
//        Vehicle vehicle = VEHICLES.get(id);
//        if (vehicle == null) {
//            return Response.status(404).build();
//        }
//
//        try {
//            vehicle.setStatus(body.status());
//        } catch (RuntimeException e) {
//            return Response.status(Response.Status.CONFLICT).build(); // ou mais genérico BAD_REQUEST (400)
//        }
//
//        VEHICLES.put(id, vehicle);
//
//        return Response.noContent().build();
//    }

}