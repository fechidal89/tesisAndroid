package ucv.tesis.tesisandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.SystemClock;

public class DBOIDHelper {
	
	public static final String DB_NAME = "obj_ident";
	public static final String DB_TABLE = "config_oids";
	public static final int VERSION = 1;
	public static final String CLASSNAME = DBOIDHelper.class.getSimpleName();
	public static final String[] COLS = {"_ID", "name", "value"};
	
	private SQLiteDatabase db;
	private DBOpenHelper dbOpenHelper=null;
	

	public class ObjIdent{
	
		public ObjIdent(long id, String name, String value) {
			super();
			this.id = id;
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			return "ObjIdent [id=" + id + ", name=" + name + ", value=" + value
					+ "]";
		}
		
		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
			private long id;
			private String name;
			private String value;
	}
	
	
	public class DBOpenHelper extends SQLiteOpenHelper {

		private static final String CREATE_TABLE = "CREATE TABLE "
												   +DBOIDHelper.DB_TABLE
												   +" (_id INTEGER PRIMARY KEY autoincrement,"
												   +" name TEXT UNIQUE NOT NULL,"
												   +" value TEXT);";
		
		
		public DBOpenHelper(Context context, String name, int version) {
			super(context, DBOIDHelper.DB_NAME, null, DBOIDHelper.VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(CREATE_TABLE);
				ContentValues values = new ContentValues();
				ObjIdent oid = new ObjIdent(Long.valueOf(0), "1.3.6.1.2.1.1.4.0", "");
				/** System **/
				
		        String str = Build.MODEL + ", " +
	        			 Build.FINGERPRINT + ", " +
	        			 Build.BOOTLOADER + ", " +
	        			 Build.MANUFACTURER + ", " +
	        			 Build.CPU_ABI ;
		        //SysDescr
		        oid.setName("1.3.6.1.2.1.1.1.0");
				oid.setValue(str);
				values.put("name", oid.name);
				values.put("value", oid.value);
				db.insert(DB_TABLE, null, values);
				//SysObjID
				oid.setName("1.3.6.1.2.1.1.2.0");
				oid.setValue("0.0");
				values.put("name", oid.name);
				values.put("value", oid.value);
				db.insert(DB_TABLE, null, values);
				//SysUpTime
				oid.setName("1.3.6.1.2.1.1.3.0");
				oid.setValue("1");
				values.put("name", oid.name);
				values.put("value", oid.value);
				db.insert(DB_TABLE, null, values);
				oid.setName("1.3.6.1.2.1.1.4.0");
				oid.setValue("SysContact");
				values.put("name", oid.name);
				values.put("value", oid.value);
				db.insert(DB_TABLE, null, values);
				oid.setName("1.3.6.1.2.1.1.5.0");
				oid.setValue("SysName");
				values.put("name", oid.getName() );
				values.put("value", oid.getValue() );
				db.insert(DB_TABLE, null, values);
				oid.setName("1.3.6.1.2.1.1.6.0");
				oid.setValue("SysLocation");
				values.put("name", oid.getName() );
				values.put("value", oid.getValue() );
				db.insert(DB_TABLE, null, values);
				//SysServices
				oid.setName("1.3.6.1.2.1.1.7.0");
				oid.setValue("72");
				values.put("name", oid.getName() );
				values.put("value", oid.getValue() );
				db.insert(DB_TABLE, null, values);
				oid.setName("CommunityReadOnly");
				oid.setValue("public");
				values.put("name", oid.getName() );
				values.put("value", oid.getValue() );
				db.insert(DB_TABLE, null, values);
				oid.setName("CommunityReadWrite");
				oid.setValue("public");
				values.put("name", oid.getName() );
				values.put("value", oid.getValue() );
				db.insert(DB_TABLE, null, values);
				oid.setName("portSNMP");
				oid.setValue("1161");
				values.put("name", oid.getName() );
				values.put("value", oid.getValue() );
				db.insert(DB_TABLE, null, values);
				oid.setName("snmpVersionOne");
				oid.setValue("true");
				values.put("name", oid.getName() );
				values.put("value", oid.getValue() );
				db.insert(DB_TABLE, null, values);
				/** ifAdminStatus **/
				int nIf=0; // numero de interfaces
            	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
				Process p=null;
				BufferedReader in2=null;
				ArrayList<String> arrIf = new ArrayList<String>();
				// Busco cuantas interfaces existen
				try {
					p = Runtime.getRuntime().exec(cmd);
					in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
					line="";
					
				    while ((line = in2.readLine()) != null) {  
				    	nIf += 1;
				    	arrIf.add(line);					    	
				    }
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
				for(int iface = 1 ; iface <= nIf ; iface++ ){
					oid.setName("1.3.6.1.2.1.2.2.1.7."+iface);
					oid.setValue("2"); // down
					values.put("name", oid.getName() );
					values.put("value", oid.getValue() );
					db.insert(DB_TABLE, null, values);
				}
				
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onOpen(SQLiteDatabase db){
			super.onOpen(db);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DBOIDHelper.DB_TABLE);
			onCreate(db);
		}

	}

	public DBOIDHelper (Context context){
		dbOpenHelper = new DBOpenHelper(context, "WR_DATA", 1);
		establishDB();
	}

	private void establishDB() {
		if(db == null){
			db = dbOpenHelper.getWritableDatabase();
		}		
	}
	
	public void cleanup(){
		if(db != null){
			db.close();
			db  = null;
		}
	}
	
	public boolean insert(ObjIdent OID){
		ContentValues values = new ContentValues();
		values.put("name", OID.name);
		values.put("value", OID.value);
		OID.id = db.insert(DBOIDHelper.DB_TABLE, null, values);
		if (OID.id != -1) return true;
		else return false;
	}

	public void update(ObjIdent OID){
		ContentValues values = new ContentValues();
		values.put("name", OID.name);
		values.put("value", OID.value);
		db.update(DBOIDHelper.DB_TABLE, values, "_id="+OID.id, null);
	}
	
	public void update(String name, String value){
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("value", value);
		db.update(DBOIDHelper.DB_TABLE, values,"name = '" + name + "' ", null);
	}
	
	public void delete(String oid){
		db.delete(DBOIDHelper.DB_TABLE , "name = '" + oid + "' ", null);
	}
	public ObjIdent getOID(String oid){
		
		Cursor c = null;
		ObjIdent objIden = null;
		
		try {
			c = db.query(true, DBOIDHelper.DB_TABLE , DBOIDHelper.COLS , "name = '" + oid + "' ", null, null, null, null, null);
			if(c.getCount() > 0){
				c.moveToFirst();
				objIden = new ObjIdent(c.getLong(0), c.getString(1), c.getString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(!c.isClosed() && c != null){
				c.close();
			}
		}
		
		return objIden;
	}
	
	public ArrayList<ObjIdent> getAll(){
		Cursor c = null;
		ArrayList<ObjIdent> objIdens = new ArrayList<ObjIdent>();
		
		try {
			c = db.query(true, DBOIDHelper.DB_TABLE , DBOIDHelper.COLS , null, null, null, null, null, null);
			int nrows = c.getCount();
			c.moveToFirst();
			for (int i = 0; i < nrows; ++i) {
				objIdens.add(new ObjIdent(c.getLong(0), c.getString(1), c.getString(2)));
				c.moveToNext();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(!c.isClosed() && c != null){
				c.close();
			}
		}
		
		return objIdens;
	}
	
}
