package br.gov.ce.sefaz.deploy.filas;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.Singleton;

import redis.clients.jedis.Jedis;
import br.gov.ce.sefaz.deploy.entidades.Deploy;
import br.gov.ce.sefaz.deploy.uteis.ArquivoUtil;

@Singleton
public class GerenciadorDeFilasRedis implements GerenciadorDeFilasInterface {
	private Set<String> filas = new HashSet<String>();
	private Jedis jedis;

	public GerenciadorDeFilasRedis() {
		pegarConexao();
	}

	private void pegarConexao() {
		this.jedis = new Jedis("172.30.121.230", 16379);
	}

	public Set<String> listarFilas() {
		return this.filas;
	}

	public Map<String, Long> listarComMensagens() {
		Map<String, Long> retorno = new HashMap<String, Long>();
		for (String key : this.filas) {
			retorno.put(key, quantasMensagensTemNaFila(key));
		}

		return retorno;
	}

	public boolean aFilaExiste(String fila) {
		return this.filas.contains(fila);
	}

	private void adicionarFila(String fila) {
		if (!aFilaExiste(fila)) {
			this.filas.add(fila);
		}
	}

	public void removerFila(String fila) {
		if (fila.isEmpty())
			return;

		if (aFilaExiste(fila)) {
			this.filas.remove(fila);
		}
	}

	public void adicionarMensagenNaFila(Deploy deploy) {
		String serializado = ArquivoUtil.toString(deploy);
		
		try {
			if (aFilaExiste(deploy.getFila())) {
				jedis.lpush(deploy.getFila(), serializado);
			} else {
				adicionarFila(deploy.getFila());
				jedis.lpush(deploy.getFila(), serializado);
			}
		} catch(RuntimeException e) {
			System.out.println("ERRO NO ACESSO AO REDIS !!!");
			pegarConexao();
			
			if (aFilaExiste(deploy.getFila())) {
				jedis.lpush(deploy.getFila(), serializado);
			} else {
				adicionarFila(deploy.getFila());
				jedis.lpush(deploy.getFila(), serializado);
			}
		}

	}

	public Deploy removerMensagenNaFila(String fila) {
		try {
			String serializado = jedis.rpop(fila);
			return (Deploy) ArquivoUtil.fromString(serializado);
		} catch(RuntimeException e) {
			System.out.println("ERRO NO ACESSO AO REDIS !!!");
			pegarConexao();
			String serializado = jedis.rpop(fila);
			return (Deploy) ArquivoUtil.fromString(serializado);
		}
	}

	public boolean temMensagemNaFila(String fila) {
		try {
			if (aFilaExiste(fila)) {
				return jedis.llen(fila) > 0;
			} else {
				adicionarFila(fila);
				return jedis.llen(fila) > 0;
			}
		} catch (RuntimeException e) {
			System.out.println("ERRO NO ACESSO AO REDIS !!!");
			pegarConexao();

			if (aFilaExiste(fila)) {
				return jedis.llen(fila) > 0;
			} else {
				adicionarFila(fila);
				return jedis.llen(fila) > 0;
			}
		}
	}

	public long quantasMensagensTemNaFila(String fila) {
		try {
			return jedis.llen(fila);
		} catch (RuntimeException e) {
			System.out.println("ERRO NO ACESSO AO REDIS !!!");
			pegarConexao();
			return jedis.llen(fila);
		}

	}

}
