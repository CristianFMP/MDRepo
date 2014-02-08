package md.smartitineraryclient.database;

public class ItineraryDB {
	private long id;
	private String elencoPOI;
	private int popolarita;
	private double lunghezza;
	private int numPOI;
	private String posUtente;
	private String datetime;
	  

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getElencoPOI() {
		return elencoPOI;
	}

	public void setElencoPOI(String elencoPOI) {
		this.elencoPOI = elencoPOI;
	}

	public int getPopolarita() {
		return popolarita;
	}

	public void setPopolarita(int popolarita) {
		this.popolarita = popolarita;
	}

	public double getLunghezza() {
		return lunghezza;
	}

	public void setLunghezza(double lunghezza) {
		this.lunghezza = lunghezza;
	}

	public int getNumPOI() {
		return numPOI;
	}

	public void setNumPOI(int numPOI) {
		this.numPOI = numPOI;
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
