package br.com.caelum.agiletickets.controllers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.com.caelum.agiletickets.domain.Agenda;
import br.com.caelum.agiletickets.domain.DiretorioDeEstabelecimentos;
import br.com.caelum.agiletickets.models.Espetaculo;
import br.com.caelum.agiletickets.models.Periodicidade;
import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockValidator;
import br.com.caelum.vraptor.validator.ValidationException;

public class EspetaculosControllerTest {

	private @Mock Agenda agenda;
	private @Mock DiretorioDeEstabelecimentos estabelecimentos;
	private @Spy Validator validator = new MockValidator();
	private @Spy Result result = new MockResult();
	
	private EspetaculosController controller;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		controller = new EspetaculosController(result, validator, agenda, estabelecimentos);
	}

	@Test(expected=ValidationException.class)
	public void naoDeveCadastrarEspetaculosSemNome() throws Exception {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setDescricao("uma descricao");

		controller.adiciona(espetaculo);

		verifyZeroInteractions(agenda);
	}

	@Test(expected=ValidationException.class)
	public void naoDeveCadastrarEspetaculosSemDescricao() throws Exception {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setNome("um nome");

		controller.adiciona(espetaculo);

		verifyZeroInteractions(agenda);
	}

	@Test
	public void deveCadastrarEspetaculosComNomeEDescricao() throws Exception {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setNome("um nome");
		espetaculo.setDescricao("uma descricao");

		controller.adiciona(espetaculo);

		verify(agenda).cadastra(espetaculo);
	}
	
	@Test
	public void deveRetornarNotFoundSeASessaoNaoExiste() throws Exception {
		when(agenda.sessao(1234l)).thenReturn(null);

		controller.sessao(1234l);

		verify(result).notFound();
	}

	@Test(expected=ValidationException.class)
	public void naoDeveReservarZeroIngressos() throws Exception {
		when(agenda.sessao(1234l)).thenReturn(new Sessao());

		controller.reserva(1234l, 0);

		verifyZeroInteractions(result);
	}

	@Test(expected=ValidationException.class)
	public void naoDeveReservarMaisIngressosQueASessaoPermite() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(3);

		when(agenda.sessao(1234l)).thenReturn(sessao);

		controller.reserva(1234l, 5);

		verifyZeroInteractions(result);
	}

	@Test
	public void deveReservarSeASessaoTemIngressosSuficientes() throws Exception {
		Espetaculo espetaculo = new Espetaculo();
		espetaculo.setTipo(TipoDeEspetaculo.TEATRO);

		Sessao sessao = new Sessao();
		sessao.setPreco(new BigDecimal("10.00"));
		sessao.setTotalIngressos(5);
		sessao.setEspetaculo(espetaculo);

		when(agenda.sessao(1234l)).thenReturn(sessao);

		controller.reserva(1234l, 3);

		assertThat(sessao.getIngressosDisponiveis(), is(2));
	}
	
	@Test
	public void deveCriarDezSessoesParaIntervaloDeDezDiasPeriocidadeDiario() {
		Espetaculo espetaculo=new Espetaculo();
		LocalDate dataInicio=new LocalDate("2015-01-01");
		LocalDate dataFim=new LocalDate("2015-01-10");
		LocalTime horario=new LocalTime("19:00");
		List<Sessao> sessoes=espetaculo.criaSessoes(dataInicio, dataFim, horario, Periodicidade.DIARIA);
		assertEquals(10,sessoes.size());
		int i=0;
		for(Sessao sessao:sessoes) {
			LocalDate ld = dataInicio.plusDays(i++);
			DateTime dt = new DateTime(ld.getYear(),ld.getMonthOfYear(),ld.getDayOfMonth(),horario.getHourOfDay(),horario.getMinuteOfHour());
			assertEquals(dt, sessao.getInicio());
		}
	}
	
	@Test
	public void deveCriarTresSessoesParaIntervaloDeTresSemanasPeriocidadeSemanal() {
		Espetaculo espetaculo=new Espetaculo();
		LocalDate dataInicio=new LocalDate("2015-01-01");
		LocalDate dataFim=new LocalDate("2015-01-21");
		LocalTime horario=new LocalTime("19:00");
		List<Sessao> sessoes=espetaculo.criaSessoes(dataInicio, dataFim, horario, Periodicidade.SEMANAL);
		assertEquals(3,sessoes.size());
		int i=0;
		for(Sessao sessao:sessoes) {
			LocalDate ld = dataInicio.plusWeeks(i++);
			DateTime dt = new DateTime(ld.getYear(),ld.getMonthOfYear(),ld.getDayOfMonth(),horario.getHourOfDay(),horario.getMinuteOfHour());
			assertEquals(dt, sessao.getInicio());
		}
	}

}
