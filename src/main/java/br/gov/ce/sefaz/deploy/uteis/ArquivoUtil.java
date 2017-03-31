package br.gov.ce.sefaz.deploy.uteis;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;


public class ArquivoUtil {
	
	public static String copiaParaStorageEPegaNovoCaminho(String caminhoDoArquivo, String destino) {
		return ArquivoUtil.moverParaStorage(ArquivoUtil.renomear(caminhoDoArquivo, "_DETECTADO"), destino);
	}

	public static String renomear(String caminhoDoArquivo, String sulfixo) {

		String novoArquivo = caminhoDoArquivo + sulfixo;

		try {
			Files.move(Paths.get(caminhoDoArquivo), Paths.get(novoArquivo), REPLACE_EXISTING);
		
		} catch (IOException e) {
			System.out.println("Erro ao tentar renomear o aquivo: " + caminhoDoArquivo + " para: " + novoArquivo);
		}
		return novoArquivo;
	}
	
	public static String moverParaStorage(String caminhoDoArquivo, String caminhoDoStorage) {
		
		String checksum = "";
		String novoArquivo = "";
		
		// verificar caminho do storage
		if(!Files.exists(Paths.get(caminhoDoStorage))) {
			try { Files.createDirectory(Paths.get(caminhoDoStorage)); } catch (IOException e2) { }
		}
		
		// criar o hash
		try {
			checksum = FileChecksum.getFileChecksum(
					MessageDigest.getInstance("MD5"), new File(caminhoDoArquivo));
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// copiar o arquivo para o storage
		novoArquivo = caminhoDoStorage + checksum;
		try {
			Files.copy(Paths.get(caminhoDoArquivo), Paths.get(novoArquivo), REPLACE_EXISTING);
			
		} catch (IOException e) {
			System.out.println("Erro ao copiar o arquivo: " + caminhoDoArquivo + " para o storage: " + caminhoDoStorage);
		}

		return novoArquivo;

	}
	
	public static String toString( Serializable o ) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream so;
		try {
			so = new ObjectOutputStream(bo);
			so.writeObject(o);
		    so.flush();
		} catch (IOException e) {
			System.out.println("Erro ao serializar objeto !");
			e.printStackTrace();
		}
       
        
        return new String(DatatypeConverter.printBase64Binary(bo.toByteArray()));
    }
	
	public static Object fromString( String s ) {	
		byte b[] = DatatypeConverter.parseBase64Binary(s);
        ByteArrayInputStream bi = new ByteArrayInputStream(b);
        
		try {
			ObjectInputStream si = new ObjectInputStream(bi);
			return si.readObject();
		} catch (IOException e) {
			System.out.println("Erro ao desserializar objeto !");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Erro ao desserializar objeto, class not found !");
			e.printStackTrace();
		}
		
        return null;
	}
}
