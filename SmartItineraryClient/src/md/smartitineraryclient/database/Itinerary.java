package md.smartitineraryclient.database;

public class Itinerary {
	private long id;
	private String nicknameUtente;
	private String elencoPOI;
	private int popolarità;
	private double lunghezza;
	private int numPOI;
	  

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNicknameUtente() {
		return nicknameUtente;
	}

	public void setNicknameUtente(String nicknameUtente) {
		this.nicknameUtente = nicknameUtente;
	}

	public String getElencoPOI() {
		return elencoPOI;
	}

	public void setElencoPOI(String elencoPOI) {
		this.elencoPOI = elencoPOI;
	}

	public int getPopolarità() {
		return popolarità;
	}

	public void setPopolarità(int popolarità) {
		this.popolarità = popolarità;
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
}
