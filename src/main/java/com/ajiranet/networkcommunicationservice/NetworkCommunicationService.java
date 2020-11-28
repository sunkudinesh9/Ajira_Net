package com.ajiranet.networkcommunicationservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import com.ajiranet.networkcommunicationservice.exceptionhandler.DeviceNotFoundException;
import com.ajiranet.networkcommunicationservice.exceptionhandler.BadRequest;
import com.ajiranet.networkcommunicationservice.models.Device;
import com.ajiranet.networkcommunicationservice.models.ResponseModel;
import com.ajiranet.networkcommunicationservice.models.StrengthValue;

@Service
public class NetworkCommunicationService {

	@Autowired
	private NetworkCommunicationDAO networkCommunicationDAO;

	/**
	 * Method to Create the devices
	 * 
	 * @param device
	 * @return ResponseModel with Http status code
	 */
	public ResponseEntity<ResponseModel> createDevice(Device device) {
		ResponseModel responseModel = new ResponseModel();
		ResponseEntity<ResponseModel> responseEntity = null;
		if (device != null) {
			if (device.getType().equalsIgnoreCase("Computer") || device.getType().equalsIgnoreCase("Repeater")) {
				Boolean flag = false;
				for (Device insideDevice : fetchDevices()) {
					if (insideDevice.getName().equalsIgnoreCase(device.getName())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					throw new BadRequest("Device " + device.getName() + " already exists");
				} else {
					networkCommunicationDAO.save(device);
					responseModel.setMsg("Successfully added " + device.getName());
					responseEntity = new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
				}

			} else {
				throw new BadRequest("Type " + device.getType() + " is not supported");
			}
		} else {
			throw new HttpMessageNotReadableException("Invalid Command");
		}
		return responseEntity;
	}

	/**
	 * Method to fetch all the devices
	 * 
	 * @return List of devices
	 */
	public List<Device> fetchDevices() {
		List<Device> devices = new ArrayList<Device>();
		networkCommunicationDAO.findAll().forEach(devices::add);
		return devices;
	}

	/**
	 * Method to modify the device strength
	 * 
	 * @param strengthValue
	 * @param deviceName
	 * @return
	 */
	public ResponseEntity<StrengthValue> modifyStrength(StrengthValue strengthValue, String deviceName) {
		ResponseEntity<StrengthValue> responseEntity = null;
		Boolean flag = false;
		Device foundDevice = null;
		Integer value = strengthValue.getValue();
		if (value.getClass().getName().equals("java.lang.Integer")) {
			for (Device insideDevice : fetchDevices()) {
				if (insideDevice.getName().equalsIgnoreCase(deviceName)) {
					flag = true;
					foundDevice = insideDevice;
					break;
				}
			}
			if (flag && foundDevice != null) {
				networkCommunicationDAO.modifyStrength(foundDevice.getName(), strengthValue.getValue());
				strengthValue.setValue(foundDevice.getStrength());
				responseEntity = new ResponseEntity<StrengthValue>(strengthValue, HttpStatus.OK);
			} else {
				throw new DeviceNotFoundException();
			}
		} else {
			throw new HttpMessageNotReadableException("value should be an integer");
		}
		return responseEntity;
	}
}
