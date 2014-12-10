package br.com.caelum.agiletickets.models;

import org.junit.Assert;
import org.junit.Test;

public class SessaoTest {

	@Test
	public void deveVender5ingressosSeHa10vagas() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(10);
		
		Assert.assertTrue(sessao.podeReservar(5));
	}
	
	public void deveVender1ingressoSeHa2vagas() throws Exception {
		Sessao sessao = new Sessao();
        sessao.setTotalIngressos(2);

        Assert.assertTrue(sessao.podeReservar(1));
	}

	@Test
	public void naoDeveVender3ingressoSeHa2vagas() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(2);

		Assert.assertFalse(sessao.podeReservar(3));
	}

	
	@Test
	public void deveVender3ingressoSeHa3vagas() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(3);

		Assert.assertTrue(sessao.podeReservar(3));
	}

	
	@Test
	public void reservarIngressosDeveDiminuirONumeroDeIngressosDisponiveis() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(5);

		sessao.reserva(3);
		Assert.assertEquals(2, sessao.getIngressosDisponiveis().intValue());
	}
	
	@Test
	public void reservarIngressosDeveDiminuirTodosIngressosDisponiveis() throws Exception {
		Sessao sessao = new Sessao();
		sessao.setTotalIngressos(5);

		sessao.reserva(5);
		Assert.assertEquals(0, sessao.getIngressosDisponiveis().intValue());
	}
	
}