package br.gov.ce.sefaz.deploy.filas;

import java.util.Map;
import java.util.Set;

import br.gov.ce.sefaz.deploy.entidades.Deploy;

public interface GerenciadorDeFilasInterface {
	public Set<String> listarFilas();
	public Map<String, Long> listarComMensagens();
	public boolean aFilaExiste(String fila);
	public void removerFila(String fila);
	public void adicionarMensagenNaFila(Deploy deploy);
	public Deploy removerMensagenNaFila(String fila);
	public boolean temMensagemNaFila(String fila);
	public long quantasMensagensTemNaFila(String fila);
	
}
