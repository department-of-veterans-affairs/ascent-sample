package gov.va.ascent.demo.service.rest.provider;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoRequest;
import gov.va.ascent.demo.partner.person.ws.client.transfer.PersonInfoResponse;
import gov.va.ascent.demo.service.api.DemoPersonService;
import gov.va.ascent.demo.service.api.DemoService;
import gov.va.ascent.demo.service.api.v1.transfer.EchoHostServiceResponse;
import gov.va.ascent.demo.service.api.v1.transfer.Host;
import gov.va.ascent.demo.service.api.v1.transfer.DemoServiceRequest;
import gov.va.ascent.demo.service.api.v1.transfer.DemoServiceResponse;
import gov.va.ascent.framework.exception.WssRuntimeException;
import gov.va.ascent.framework.service.ServiceResponse;
import gov.va.ascent.framework.swagger.SwaggerResponseMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class DemoServiceEndpoint implements HealthIndicator, SwaggerResponseMessages {

	private final static Logger LOGGER = LoggerFactory.getLogger(DemoServiceEndpoint.class);

	@Autowired
	@Qualifier("IMPL")
	DemoService demoService;
	
	@Autowired
	@Qualifier("IMPL")
	DemoPersonService demoPersonService;
	
	public static final String URL_PREFIX = "/demo/v1";
	
	//TODO make this method a REST call to test this endpoint is up and running
	@RequestMapping(value = URL_PREFIX + "/health", method = RequestMethod.GET)
	@ApiOperation(value = "A health check of this endpoint", notes = "Will perform a basic health check to see if the operation is running.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = Health.class, message = MESSAGE_200),
			@ApiResponse(code = 500, response = Health.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
	public Health health() {
        return Health.up().withDetail("Demo Service REST Endpoint", "Demo Service REST Provider Up and Running!").build();
    } 
	
	@RequestMapping(value = URL_PREFIX + "/echo", method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EchoHostServiceResponse> echo(HttpServletRequest request) {
		InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new WssRuntimeException(e);
        }
        EchoHostServiceResponse response = new EchoHostServiceResponse();
        Host host = new Host();
        host.setHostName(addr.getHostName());
        host.setLocalPort(request.getLocalPort());
        host.setLocalAddress(request.getLocalAddr());
        response.setHost(host);
        LOGGER.info("ECHO SERVICE INVOKED: " + response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	@RequestMapping(value = URL_PREFIX + "/create", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
	@ApiOperation(value = "Creates a DEMO.", notes = "Will create and persist a DEMO entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ServiceResponse.class, message = MESSAGE_200),
			@ApiResponse(code = 400, response = ServiceResponse.class, message = MESSAGE_400), 
			@ApiResponse(code = 500, response = ServiceResponse.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
	public ResponseEntity<ServiceResponse> create(@RequestBody DemoServiceRequest request) {
		return new ResponseEntity<>(demoService.create(request), HttpStatus.OK);
	}
	
	@RequestMapping(value = URL_PREFIX + "/read/{name}", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ApiOperation(value = "Reads a DEMO.", notes = "Will retrieve and return a previously created DEMO entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = DemoServiceResponse.class, message = MESSAGE_200),
			@ApiResponse(code = 400, response = ServiceResponse.class, message = MESSAGE_400), 
			@ApiResponse(code = 500, response = ServiceResponse.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
	public ResponseEntity<DemoServiceResponse> read(@PathVariable String name) {
		return new ResponseEntity<>(demoService.read(name), HttpStatus.OK);
	}
	
	@RequestMapping(value = URL_PREFIX + "/readAsync/{name}", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	@ApiOperation(value = "Reads a DEMO.", notes = "Will retrieve and return a previously created DEMO entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = DemoServiceResponse.class, message = MESSAGE_200),
			@ApiResponse(code = 400, response = ServiceResponse.class, message = MESSAGE_400), 
			@ApiResponse(code = 500, response = ServiceResponse.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
	public ResponseEntity<DemoServiceResponse> readAsync(@PathVariable String name) {
		try {
			Future<DemoServiceResponse> futureDemoResponse = demoService.readAsync(name);
			while (!futureDemoResponse.isDone()) {
				continue;
			}
			return new ResponseEntity<>(futureDemoResponse.get(), HttpStatus.OK);
		} catch (InterruptedException | ExecutionException e) {
			return new ResponseEntity<>(new DemoServiceResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = URL_PREFIX + "/update", produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
	@ApiOperation(value = "Updates a DEMO.", notes = "Will update a preveiously created DEMO entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ServiceResponse.class, message = MESSAGE_200),
			@ApiResponse(code = 400, response = ServiceResponse.class, message = MESSAGE_400), 
			@ApiResponse(code = 500, response = ServiceResponse.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
	public ResponseEntity<ServiceResponse> update(@RequestBody DemoServiceRequest request) {
		return new ResponseEntity<>(demoService.update(request), HttpStatus.OK);
	}

	@RequestMapping(value = URL_PREFIX + "/delete/{name}", produces=MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	@ApiOperation(value = "Deletes a DEMO.", notes = "Will delete a previously created DEMO entity.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ServiceResponse.class, message = MESSAGE_200),
			@ApiResponse(code = 400, response = ServiceResponse.class, message = MESSAGE_400), 
			@ApiResponse(code = 500, response = ServiceResponse.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
	public ResponseEntity<ServiceResponse> delete(@PathVariable String name) {
		return new ResponseEntity<>(demoService.delete(name), HttpStatus.OK);
	}
	
	@RequestMapping(value = URL_PREFIX + "/person/ssn", 
			produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
	@ApiOperation(value = "SSN based Person Info from DEMO Partner Service.", notes = "Will return a person info based on SSN.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ServiceResponse.class, message = MESSAGE_200),
			@ApiResponse(code = 400, response = ServiceResponse.class, message = MESSAGE_400), 
			@ApiResponse(code = 500, response = ServiceResponse.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
	public ResponseEntity<PersonInfoResponse> personBySSN(@RequestBody PersonInfoRequest personInfoRequest) {
		try {
			return new ResponseEntity<>(demoPersonService.getPersonInfo(personInfoRequest), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new PersonInfoResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = URL_PREFIX + "/person/pid", 
			produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE,  method = RequestMethod.POST)
	@ApiOperation(value = "PID based Person Info from DEMO Partner Service.", notes = "Will return a person info based on PID.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, response = ServiceResponse.class, message = MESSAGE_200),
			@ApiResponse(code = 400, response = ServiceResponse.class, message = MESSAGE_400), 
			@ApiResponse(code = 500, response = ServiceResponse.class, message = MESSAGE_500),
			@ApiResponse(code = 403, message = MESSAGE_403) })
	public ResponseEntity<PersonInfoResponse> personByPid(@RequestBody PersonInfoRequest personInfoRequest) {
		return new ResponseEntity<>(demoPersonService.findPersonByParticipantID(personInfoRequest), HttpStatus.OK);
	}

}
