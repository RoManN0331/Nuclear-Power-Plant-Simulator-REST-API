package de.uni_trier.restapi_vr.controller;import de.uni_trier.restapi_vr.service.SystemService;import de.uni_trier.restapi_vr.simulator.DTO.Component_DTO;import de.uni_trier.restapi_vr.simulator.DTO.Status_DTO;import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;import io.swagger.v3.oas.annotations.Operation;import io.swagger.v3.oas.annotations.responses.ApiResponse;import io.swagger.v3.oas.annotations.responses.ApiResponses;import io.swagger.v3.oas.annotations.tags.Tag;import jakarta.annotation.Resource;import jakarta.inject.Inject;import jakarta.ws.rs.GET;import jakarta.ws.rs.POST;import jakarta.ws.rs.Path;import jakarta.ws.rs.Produces;import jakarta.ws.rs.container.AsyncResponse;import jakarta.ws.rs.container.Suspended;import jakarta.ws.rs.core.MediaType;import jakarta.ws.rs.core.Response;import java.util.List;import java.util.Map;import java.util.concurrent.ExecutorService;import java.util.concurrent.Executors;/** * Package: com.example.restapi_vr.controller */@Path("/system")@Tag(name = "System Endpoints")public class SystemController {    @Resource    ExecutorService executorService;    @Resource    private final SystemService systemService = new SystemService(NPPSystemInterface.getInstance());    @Operation(            summary = "Retrieve server and simulation status",            description = "Returns information about the server and whether the simulation is running, including the start time if it's running."    )    @ApiResponses({            @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),            @ApiResponse(responseCode = "500", description = "Problem with Reactor or Server")    })    @GET    @Path("/status")    @Produces(MediaType.APPLICATION_JSON)    public void getStatus(@Suspended final AsyncResponse response) {        executorService = Executors.newSingleThreadExecutor();        executorService.submit(() -> {            try {                Status_DTO status = systemService.getStatus();                response.resume(Response.ok(status).build());            } catch (Exception e) {                response.resume(e);            }        });        executorService.shutdown();    }    @Operation(            summary = "Get reactor components",            description = "Retrieves a list of reactor components, including their name, blown_status, and interactability."    )    @ApiResponses({            @ApiResponse(responseCode = "200", description = "Components retrieved successfully"),            @ApiResponse(responseCode = "500", description = "Problem with Reactor or Server")    })    @GET    @Path("/components")    @Produces(MediaType.APPLICATION_JSON)    public void getComponents(@Suspended final AsyncResponse response) {        executorService = Executors.newSingleThreadExecutor();        executorService.submit(() -> {            try {                List<Component_DTO> components = systemService.getComponents();                response.resume(Response.ok(Map.of("components", components)).build());            } catch (Exception e) {                response.resume(e);            }        });        executorService.shutdown();    }    @Operation(            summary = "Restart Reactor Simulation",            description = "shutdown current running simulation and restart simulation in OFF State"    )    @ApiResponses({            @ApiResponse(responseCode = "200", description = "Reactor simulation restarted successfully"),            @ApiResponse(responseCode = "500", description = "Problem with Server")    })    @POST    @Path("/restart")    @Produces(MediaType.APPLICATION_JSON)    public void restart(@Suspended final AsyncResponse response) {        executorService = Executors.newSingleThreadExecutor();        executorService.submit(() -> {            try {                systemService.restartSimulation();                response.resume(Response.ok(Map.of("message","Reactor simulation restarted successfully")).build());            } catch (Exception e) {                response.resume(e);            }        });        executorService.shutdown();    }}