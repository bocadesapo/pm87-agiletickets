package br.com.caelum.agiletickets.models;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class CalculoSessoesDiarias implements CalculoSessoes{

	@Override
	public List<Sessao> getSessoes(Espetaculo espetaculo, LocalDate inicio, LocalDate fim,
			LocalTime horario) {
		List<Sessao> sessoes = newArrayList();
		int dias=Days.daysBetween(inicio, fim).getDays()+1;
		for (int i = 0; i < dias; i++) {
			LocalDate data=inicio.plusDays(i);
			DateTime dt = new DateTime(data.getYear(),data.getMonthOfYear(),data.getDayOfMonth(),horario.getHourOfDay(),horario.getMinuteOfHour());
			Sessao sessao=new Sessao();
			sessao.setInicio(dt);
			sessao.setEspetaculo(espetaculo);
			sessoes.add(sessao);
		}
		return sessoes;
	}

}
