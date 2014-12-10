package br.com.caelum.agiletickets.models;

import java.util.List;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public interface CalculoSessoes {
	
	 List<Sessao> getSessoes(Espetaculo espetaculo, LocalDate inicio, LocalDate fim, LocalTime horario);

}
