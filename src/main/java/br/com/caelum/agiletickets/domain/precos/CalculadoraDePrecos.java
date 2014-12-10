package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {
	
	private static final double PERCENTUAL_NAO_RESERVADO_CINEMA_SHOW=0.05;
	private static final double PERCENTUAL_NAO_RESERVADO_BALLET_ORQUESTRA=0.50;
	private static final double PERCENTUAL_REAJUSTE_CINEMA_SHOW=0.10;
	private static final double PERCENTUAL_REAJUSTE_BALLET_ORQUESTRA=0.20;
	private static final double PERCENTUAL_REAJUSTE_DURACAO=0.10;

	public static BigDecimal calcula(Sessao sessao, Integer quantidade) {
		BigDecimal preco;
		switch(sessao.getEspetaculo().getTipo()){
			case CINEMA:
			case SHOW:
				//quando estiverem acabando os ingressos... 
				preco=aplicarAjustePreco(sessao,PERCENTUAL_NAO_RESERVADO_CINEMA_SHOW,PERCENTUAL_REAJUSTE_CINEMA_SHOW);
				break;
			case BALLET:
			case ORQUESTRA:
				preco=aplicarAjustePreco(sessao,PERCENTUAL_NAO_RESERVADO_BALLET_ORQUESTRA,PERCENTUAL_REAJUSTE_BALLET_ORQUESTRA);
				
				if(sessao.getDuracaoEmMinutos() > 60){
					preco = preco.add(sessao.getPreco().multiply(BigDecimal.valueOf(PERCENTUAL_REAJUSTE_DURACAO)));
				}
				break;
			default:
				preco = sessao.getPreco();
			}
		
		return preco.multiply(BigDecimal.valueOf(quantidade));
		
	}
	
	private static BigDecimal aplicarAjustePreco(Sessao sessao, double percentualNaoReservado, double percentualReajuste) {
		if(sessao.getPercentualReservado() <= percentualNaoReservado) { 
			return sessao.getPreco().add(sessao.getPreco().multiply(BigDecimal.valueOf(percentualReajuste)));
		} else {
			return sessao.getPreco();
		}
	}

}