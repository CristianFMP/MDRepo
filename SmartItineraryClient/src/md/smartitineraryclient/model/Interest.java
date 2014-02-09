package md.smartitineraryclient.model;

public class Interest {
	
	private long id;
	private String categoria;
	private String macrocategoria;
	private String dataInserimento;
	private String dataCancellazione;
	
	public Interest(String cat, String macrocat, String dataIns, String dataCanc) {
		this.categoria = cat;
		this.macrocategoria = macrocat;
		this.dataInserimento = dataIns;
		this.dataCancellazione = dataCanc;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	
	public String getDataCancellazione() {
		return dataCancellazione;
	}
	public void setDataCancellazione(String dataCancellazione) {
		this.dataCancellazione = dataCancellazione;
	}
	
	
}
