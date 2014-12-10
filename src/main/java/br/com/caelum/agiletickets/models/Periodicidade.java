package br.com.caelum.agiletickets.models;

public enum Periodicidade {
	
	DIARIA(new CalculoSessoesDiarias()),
	SEMANAL(new CalculoSessoesSemanais());
	
	private CalculoSessoes calculoSessoes;
	
	private Periodicidade(CalculoSessoes calculoSessoes) {
		this.calculoSessoes=calculoSessoes;
	}
	
	public CalculoSessoes getCalculoSessoes() {
		return calculoSessoes;
	}
}
