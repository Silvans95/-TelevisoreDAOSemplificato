package it.prova.test;

import java.util.Date;
import java.util.List;

import it.prova.model.Televisore;
import it.prova.service.MyServiceFactory;
import it.prova.service.televisore.TelevisoreService;

public class TestTelevisore {

	public static void main(String[] args) {

		// parlo direttamente con il service
		TelevisoreService televisoreService = MyServiceFactory.getTelevisoreServiceImpl();

		try {

			// ora con il service posso fare tutte le invocazioni che mi servono
			System.out.println("In tabella ci sono " + televisoreService.listAll().size() + " elementi.");

			testInserimentoNuovoTelevisore(televisoreService);
			System.out.println("In tabella ci sono " + televisoreService.listAll().size() + " elementi.");

			testRimozioneUser(televisoreService);
			System.out.println("In tabella ci sono " + televisoreService.listAll().size() + " elementi.");

			testFindByExample(televisoreService);
			System.out.println("In tabella ci sono " + televisoreService.listAll().size() + " elementi.");

			testUpdateUser(televisoreService);
			System.out.println("In tabella ci sono " + televisoreService.listAll().size() + " elementi.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void testInserimentoNuovoTelevisore(TelevisoreService televisoreService) throws Exception {
		System.out.println(".......testInserimentoNuovoTelevisore inizio.............");
		Televisore newTelevisoreInstance = new Televisore("Toshiba", "xxx", new Date());
		if (televisoreService.inserisciNuovo(newTelevisoreInstance) != 1)
			throw new RuntimeException("testInserimentoNuovoTelevisore fallito ");

		System.out.println("inserito nuovo record: " + newTelevisoreInstance);
		System.out.println(".......testInserimentoNuovoUser fine.............");
	}

	private static void testRimozioneUser(TelevisoreService televisoreService) throws Exception {
		System.out.println(".......testRimozioneUser inizio.............");
		// recupero tutti gli user
		List<Televisore> interoContenutoTabella = televisoreService.listAll();
		if (interoContenutoTabella.isEmpty() || interoContenutoTabella.get(0) == null)
			throw new Exception("Non ho nulla da rimuovere");

		Long idDelPrimo = interoContenutoTabella.get(0).getId();
		// ricarico per sicurezza con l'id individuato
		Televisore toBeRemoved = televisoreService.findById(idDelPrimo);
		System.out.println("User candidato alla rimozione: " + toBeRemoved);
		if (televisoreService.rimuovi(toBeRemoved) != 1)
			throw new RuntimeException("testRimozioneUser fallito ");

		System.out.println("rimosso record: " + toBeRemoved);
		System.out.println(".......testRimozioneUser fine.............");
	}

	private static void testFindByExample(TelevisoreService televisoreService) throws Exception {
		System.out.println(".......testFindByExample inizio.............");
		// inserisco i dati che poi mi aspetto di ritrovare
		televisoreService.inserisciNuovo(new Televisore("Samsung", "tele", new Date()));
		televisoreService.inserisciNuovo(new Televisore("Samsung", "TV", new Date()));

		// preparo un example che ha come nome e ricerco
		List<Televisore> risultatifindByExample = televisoreService.findByExample(new Televisore("Sam", "t"));
		if (risultatifindByExample.size() != 2)
			throw new RuntimeException("testFindByExample fallito ");

		// se sono qui il test è ok quindi ripulisco i dati che ho inserito altrimenti
		// la prossima volta non sarebbero 2 ma 4, ecc.
		for (Televisore userItem : risultatifindByExample) {
			televisoreService.rimuovi(userItem);
		}

		System.out.println(".......testFindByExample fine.............");
	}

	private static void testUpdateUser(TelevisoreService televisoreService) throws Exception {
		System.out.println(".......testUpdateUser inizio.............");

		// inserisco i dati che poi modifico
		if (televisoreService.inserisciNuovo(new Televisore("Lenovo", "tv", new Date())) != 1)
			throw new RuntimeException("testUpdateUser: inserimento preliminare fallito ");

		// recupero col findbyexample e mi aspetto di trovarla
		List<Televisore> risultatifindByExample = televisoreService.findByExample(new Televisore("Lenovo", "tv"));
		if (risultatifindByExample.size() != 1)
			throw new RuntimeException("testUpdateUser: testFindByExample fallito ");

		Long idHP = risultatifindByExample.get(0).getId();
		// ricarico per sicurezza con l'id individuato e gli modifico un campo
		String nuovoModello = "pavillon";
		Televisore toBeUpdated = televisoreService.findById(idHP);
		toBeUpdated.setModello(nuovoModello);
		System.out.println("User candidato alla modifica: " + toBeUpdated);
		if (televisoreService.aggiorna(toBeUpdated) != 1)
			throw new RuntimeException("testUpdateUser fallito ");

		System.out.println("aggiornato record: " + toBeUpdated);
		System.out.println(".......testUpdateUser inizio.............");
	}

//		##############################################################################################
//		##############################################################################################
//		##############################################################################################
}
