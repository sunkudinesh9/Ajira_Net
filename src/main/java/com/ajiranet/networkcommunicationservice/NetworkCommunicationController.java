package com.ajiranet.networkcommunicationservice;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajiranet.networkcommunicationservice.models.Connection;
import com.ajiranet.networkcommunicationservice.models.Device;
import com.ajiranet.networkcommunicationservice.models.ResponseModel;
import com.ajiranet.networkcommunicationservice.models.StrengthValue;

@RestController
@RequestMapping("/ajiranet/process")
public class NetworkCommunicationController {

	@Autowired
	private NetworkCommunicationService networkCommunicationService;

	@RequestMapping(value = "/create/devices", method = RequestMethod.POST)
	public ResponseEntity<ResponseModel> createDevice(@Valid @RequestBody Device deice) {
		return networkCommunicationService.createDevice(deice);
	}

	@RequestMapping(value = "/fetch/devices")
	public List<Device> fetchDevices() {
		return networkCommunicationService.fetchDevices();
	}

	@RequestMapping(value = "/modify/devices/{devicename}/strength", method = RequestMethod.POST)
	public ResponseEntity<ResponseModel> modifyStrength(@Valid @RequestBody StrengthValue strengthValue,
			@PathVariable(value = "devicename") String deviceName) {
		return networkCommunicationService.modifyStrength(strengthValue, deviceName);
	}

	@RequestMapping(value = "/create/connection")
	public ResponseEntity<ResponseModel> createConnection(@Valid @RequestBody Connection connection) {
		return networkCommunicationService.createConnection(connection);
	}

	@RequestMapping(value = "/fetch/info-routes")
	public ResponseEntity<ResponseModel> routeInfo(@RequestParam String from, @RequestParam String to) {
		return networkCommunicationService.routInfo(from, to);
	}

}
