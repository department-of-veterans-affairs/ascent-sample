package gov.va.ascent.demo.service.api;

import java.util.concurrent.Future;

import gov.va.ascent.demo.service.api.v1.transfer.DemoServiceResponse;

public interface DemoService {

  /**
   * Reads the DemoServiceResponse.
   *
   * @param name Read Name
   * @return A DemoServiceResponse instance
   */
	DemoServiceResponse read(String name);
	
	/**
	* Reads the DemoServiceResponse.
	*
	* @param name Read Name
	* @return A Future<DemoServiceResponse instance
	*/
	Future<DemoServiceResponse> readAsync(String id);
}
