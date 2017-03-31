package br.gov.ce.sefaz.deploy.filas;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import javax.ejb.Singleton;

import br.gov.ce.sefaz.deploy.entidades.Deploy;

@Singleton
public class GerenciadorDeFilas implements GerenciadorDeFilasInterface {
	private Map<String, Queue<Deploy>> filas = new HashMap<String, Queue<Deploy>>();
	
	public GerenciadorDeFilas() {
		
	} 
	
	public Set<String> listarFilas() {
		return this.filas.keySet();
	}
	
	public Map<String, Long> listarComMensagens() {
		Map<String, Long> retorno = new HashMap<String, Long>();
		for(String key :this.filas.keySet()) {
			Queue<Deploy> q = this.filas.get(key);
			retorno.put(key, new Long(q.size()));
		}
		
		return retorno;
	}
	
	public boolean aFilaExiste(String fila) {
		return this.filas.containsKey(fila);
	}
	
	private void adicionarFila(String fila) {
		if(!aFilaExiste(fila)) {
			Queue<Deploy> q = new LinkedList<Deploy>();
			this.filas.put(fila, q);
		}
	}
	
	public void removerFila(String fila) {
		if(aFilaExiste(fila)) {
			this.filas.remove(fila);
		}
	}
	
	public void adicionarMensagenNaFila(Deploy deploy) {
		System.out.println("Novo deploy enviado para a fila: " + deploy);
		
		if(aFilaExiste(deploy.getFila())) {
			Queue<Deploy> q = this.filas.get(deploy.getFila());
			q.add(deploy);
		} else {
			adicionarFila(deploy.getFila());
			Queue<Deploy> q = this.filas.get(deploy.getFila());
			q.add(deploy);
		}
	}
	
	public Deploy removerMensagenNaFila(String fila) {
		if(aFilaExiste(fila)) {
			Queue<Deploy> q = this.filas.get(fila);
			return q.remove();
		}
		
		return null;
	}
	
	public boolean temMensagemNaFila(String fila) {
		if(aFilaExiste(fila)) {
			Queue<Deploy> q = this.filas.get(fila);
			return ! q.isEmpty();
		} else {
			adicionarFila(fila);
			Queue<Deploy> q = this.filas.get(fila);
			return ! q.isEmpty();
		}
	}
	
	public long quantasMensagensTemNaFila(String fila) {
		if(aFilaExiste(fila)) {
			Queue<Deploy> q = this.filas.get(fila);
			return q.size();
		}
		
		return 0;
	}
}
