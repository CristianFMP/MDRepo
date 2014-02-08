package md.smartitineraryclient.db;

import android.provider.BaseColumns;

public interface InterestTable extends BaseColumns {
	String TABLE_NAME = "interessi";
 
	String CATEGORIA = "categoria";
	String MACROCATEGORIA = "macrocategoria";
	String DATA_INSERIMENTO = "dataInserimento";
	String DATA_CANCELLAZIONE = "dataCancellazione";
 
	String[] COLUMNS = new String[]
	{ _ID, CATEGORIA, MACROCATEGORIA, DATA_INSERIMENTO, DATA_CANCELLAZIONE };
}
