package com.ajiranet.networkcommunicationservice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ajiranet.networkcommunicationservice.models.Device;
import com.ajiranet.networkcommunicationservice.models.StrengthValue;

@Repository
public class NetworkCommunicationDAO {
	List<Device> deviceList = new ArrayList<Device>();

	public void save(Device device) {
		deviceList.add(device);
	}

	public List<Device> findAll() {
		return deviceList;
	}

	public StrengthValue modifyStrength(String deviceName, int strength) {
		StrengthValue strengthValue = new StrengthValue();
		for (Device device : deviceList) {
			if (device.getName().equalsIgnoreCase(deviceName)) {
				device.setStrength(strength);
				strengthValue.setValue(strength);
				break;
			}
		}
		return strengthValue;
	}
}
