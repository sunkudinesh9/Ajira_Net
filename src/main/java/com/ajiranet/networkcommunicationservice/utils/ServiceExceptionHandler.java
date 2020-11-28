package com.ajiranet.networkcommunicationservice.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ajiranet.networkcommunicationservice.exceptionhandler.DeviceNotFoundException;
import com.ajiranet.networkcommunicationservice.exceptionhandler.BadRequest;
import com.ajiranet.networkcommunicationservice.models.ResponseModel;

@RestControllerAdvice
public class ServiceExceptionHandler {

	@ExceptionHandler(BadRequest.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseModel invalidCommand(BadRequest e) {
		ResponseModel responseModel = new ResponseModel();
		responseModel.setMsg(e.getLocalizedMessage());
		return responseModel;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseModel httpMessageNotReadableException(HttpMessageNotReadableException e) {
		ResponseModel responseModel = new ResponseModel();
		if(e.getLocalizedMessage().contains("Required request body is missing")) {
			responseModel.setMsg("Invalid Command");
		} else if(e.getLocalizedMessage().contains("JSON parse error")) {
			responseModel.setMsg("value should be an integer");
		}
		return responseModel;
	}

	@ExceptionHandler(DeviceNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ResponseModel deviceNotFoundException(DeviceNotFoundException deviceNotFound) {
		ResponseModel responseModel = new ResponseModel();
		responseModel.setMsg(deviceNotFound.getMessage());
		return responseModel;
	}
}
