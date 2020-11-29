package com.ajiranet.networkcommunicationservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import com.ajiranet.networkcommunicationservice.exceptionhandler.BadRequest;
import com.ajiranet.networkcommunicationservice.exceptionhandler.DeviceNotFoundException;
import com.ajiranet.networkcommunicationservice.exceptionhandler.InvalidConnectionException;
import com.ajiranet.networkcommunicationservice.exceptionhandler.NoStrengthException;
import com.ajiranet.networkcommunicationservice.models.Connection;
import com.ajiranet.networkcommunicationservice.models.Device;
import com.ajiranet.networkcommunicationservice.models.ResponseModel;
import com.ajiranet.networkcommunicationservice.models.StrengthValue;
import com.ajiranet.networkcommunicationservice.utils.Devices;

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
			if (device.getType().equalsIgnoreCase(Devices.computer.toString())
					|| device.getType().equalsIgnoreCase(Devices.repeater.toString())) {
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
	public ResponseEntity<ResponseModel> modifyStrength(StrengthValue strengthValue, String deviceName) {
		ResponseEntity<ResponseModel> responseEntity = null;
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
				ResponseModel model = new ResponseModel();
				model.setMsg("Successfully defined strength");
				responseEntity = new ResponseEntity<ResponseModel>(model, HttpStatus.OK);
			} else {
				throw new DeviceNotFoundException("No Device Found");
			}
		} else {
			throw new HttpMessageNotReadableException("value should be an integer");
		}
		return responseEntity;
	}

	public Device getDeviceByName(String deviceName) {
		Device requiredDevice = null;
		for (Device insideDevice : fetchDevices()) {
			if (insideDevice.getName().equalsIgnoreCase(deviceName)) {
				requiredDevice = insideDevice;
			}
		}
		return requiredDevice;
	}

	/**
	 * Method to fetch all Connection Flow
	 * 
	 * @return List of devices
	 */
	public List<Device> fectchConnectedDevices() {
		List<Device> connectedDevices = new ArrayList<Device>();
		networkCommunicationDAO.getConnectionFlow().forEach(connectedDevices::add);
		return connectedDevices;
	}

	/**
	 * Method to connect to each other
	 * 
	 * @param source
	 * @param target
	 */
	public ResponseEntity<ResponseModel> createConnection(Connection connection) {
		ResponseEntity<ResponseModel> responseEntity = null;
		String source = connection.getSource();
		Device sourceDevice = getDeviceByName(source);
		if (sourceDevice != null) {
			List<String> target = connection.getTarget();
			if (!target.contains(source)) {
				int strength = sourceDevice.getStrength();
				networkCommunicationDAO.connectDevice(sourceDevice);
				for (int i = 0; i < target.size(); i++) {
					if (i < strength) {
						Device targetDevice = getDeviceByName(target.get(i));
						if (targetDevice != null) {
							if (!fectchConnectedDevices().contains(targetDevice)) {
								if (targetDevice.getType().contains(Devices.repeater.toString())) {
									strength = strength + 2;
									continue;
								}
								networkCommunicationDAO.connectDevice(targetDevice);
							} else {
								throw new InvalidConnectionException("Devices are already connected");
							}
						} else {
							throw new DeviceNotFoundException("Target " + target.get(i) + " not found");
						}
					} else {
						throw new NoStrengthException("No strength to connect till last device");
					}
				}
				ResponseModel model = new ResponseModel();
				model.setMsg("Successfully connected");
				responseEntity = new ResponseEntity<ResponseModel>(model, HttpStatus.OK);
			} else {
				throw new BadRequest("Cannot connect device to itself");
			}
		} else {
			throw new DeviceNotFoundException("Node " + source + " not found");
		}
		return responseEntity;
	}

	public ResponseEntity<ResponseModel> routInfo(String source, String target) {
		List<Device> connectedDevices = networkCommunicationDAO.getConnectionFlow();
		String desplayString = "";
		if(getDeviceByName(source) != null && getDeviceByName(target) != null) {
			if (!getDeviceByName(target).getType().contains("repeater")
					&& !getDeviceByName(source).getType().contains("repeater")) {
				for (int i = 0; i < connectedDevices.size(); i++) {
					if (connectedDevices.get(i).getName().equalsIgnoreCase(source)) {
						try {
							for (int j = i; !connectedDevices.get(j).getName().equalsIgnoreCase(target); j++) {
								desplayString = desplayString + connectedDevices.get(j).getName() + "->";
							}
						} catch (Exception e) {
							throw new BadRequest("Route not found");
						}
						break;
					}
				}
			} else {
				throw new BadRequest("Route cannot be calculated with repeater");
			}
		} else{
			throw new DeviceNotFoundException("Node " + target + " not found");
		}
		desplayString = desplayString + target;
		ResponseModel model = new ResponseModel();
		model.setMsg(desplayString);
		return new ResponseEntity<ResponseModel>(model, HttpStatus.OK);
	}
}
