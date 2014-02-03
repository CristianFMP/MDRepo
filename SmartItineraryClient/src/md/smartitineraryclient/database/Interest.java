package md.smartitineraryclient.database;

public class Interest {
	
	private long id;
	private String categoria;
	private String macrocategoria;
	private String dataInserimento;
	
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
	
	
}
