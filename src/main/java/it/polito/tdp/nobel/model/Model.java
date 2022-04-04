package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private List<Esame> esami;
	private Set<Esame> migliore;
	private double mediaMigliore;
	
	public Model() {
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int m) {
		//ripristino soluzione migliore
		migliore = new HashSet<Esame>();
		mediaMigliore = 0.0;
		
		Set<Esame> parziale = new HashSet<Esame>();
		//cerca1(parziale,0,m);
		cerca2(parziale,0,m);

		return migliore;	
	}

	private void cerca2(Set<Esame> parziale, int L, int m) {
		// Controllare i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
				
		if(sommaCrediti > m) //soluzione non valida!
			return;
				
		if(sommaCrediti == m) {
			//soluzione valida! controlliamo se è la migliore (fino a qui)
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				migliore = new HashSet<Esame>(parziale);
				mediaMigliore = mediaVoti;
			}
					
			return;
		}
				
		//sicuramente, crediti < m
		if(L == esami.size())
			return;
		
		//provo ad aggiungere esami[L]
		parziale.add(esami.get(L));
		cerca2(parziale,L+1,m);
		
		//provo a "non aggiungere" esami[L]
		parziale.remove(esami.get(L));
		cerca2(parziale,L+1,m);
		
	}

	/*
	 * COMPLESSITA' N!
	 */
	private void cerca1(Set<Esame> parziale, int L, int m) {
		
		// Controllare i casi terminali
		int sommaCrediti = sommaCrediti(parziale);
		
		if(sommaCrediti > m) //soluzione non valida!
			return;
		
		if(sommaCrediti == m) {
			//soluzione valida! controlliamo se è la migliore (fino a qui)
			double mediaVoti = calcolaMedia(parziale);
			if(mediaVoti > mediaMigliore) {
				migliore = new HashSet<Esame>(parziale);
				mediaMigliore = mediaVoti;
			}
			
			return;
		}
		
		//sicuramente, crediti < m
		if(L == esami.size())
			return;
		
		//generiamo i sotto-problemi
		for(Esame e : esami) {
			if(!parziale.contains(e)) {
				parziale.add(e);
				cerca1(parziale, L+1, m);
				parziale.remove(e);
			}
		}
		
	}

	public double calcolaMedia(Set<Esame> esami) {
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) {
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
