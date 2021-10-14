package it.prova.service;

import it.prova.dao.televisore.TelevisoreDAOImpl;
import it.prova.service.televisore.TelevisoreService;
import it.prova.service.televisore.TelevisoreServiceImpl;

public class MyServiceFactory {
	
	public static TelevisoreService getTelevisoreServiceImpl() {
		TelevisoreService TelevisoreService = new TelevisoreServiceImpl();
		TelevisoreService.setTelevisoreDao(new TelevisoreDAOImpl());
		return TelevisoreService;
	}

}
