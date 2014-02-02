package md.smartitineraryclient.database;

public class Interest {
	
	private String nicknameUtente;
	private String categoria;
	private String macrocategoria;
	private String dataInserimento;
	
	public String getNicknameUtente() {
		return nicknameUtente;
	}
	public void setNicknameUtente(String nicknameUtente) {
		this.nicknameUtente = nicknameUtente;
	}
	
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getMacrocategoria() {
		return macrocategoria;
	}
	public void setMacrocategoria(String macrocategoria) {
		this.macrocategoria = macrocategoria;
	}
	
	public String getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(String dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	
}
