package gov.va.ascent.demo.service.api;

import java.util.concurrent.Future;

import gov.va.ascent.demo.service.api.v1.transfer.DemoServiceRequest;
import gov.va.ascent.demo.service.api.v1.transfer.DemoServiceResponse;
import gov.va.ascent.framework.service.ServiceResponse;

public interface DemoService {

	ServiceResponse create(DemoServiceRequest request);
	DemoServiceResponse read(String name);
	Future<DemoServiceResponse> readAsync(String id);
	ServiceResponse update(DemoServiceRequest request);
	ServiceResponse delete(String name);
}
