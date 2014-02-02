package md.smartitineraryclient.database;

public class Preference {
	private long idItinerario;
	private String nicknameUtente;
	private String posUtente;
	private String datetime;
	
	public long getIdItinerario() {
		return idItinerario;
	}
	public void setIdItinerario(long idItinerario) {
		this.idItinerario = idItinerario;
	}
	
	public String getNicknameUtente() {
		return nicknameUtente;
	}
	public void setNicknameUtente(String nicknameUtente) {
		this.nicknameUtente = nicknameUtente;
	}
	
	public String getPosUtente() {
		return posUtente;
	}
	public void setPosUtente(String posUtente) {
		this.posUtente = posUtente;
	}
	
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
}
