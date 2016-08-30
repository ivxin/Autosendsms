package com.ivxin.db;

import java.util.ArrayList;
import java.util.List;

import com.ivxin.entity.SMS;
import com.ivxin.utils.StringUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBserver {

	DBconnHelper dbh;

	public DBserver(Context context) {
		dbh = new DBconnHelper(context);
	}

	public List<SMS> selectAllSMS() {
		List<SMS> list = new ArrayList<SMS>();
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor c = db.rawQuery("select * from sms order by id desc", null);
		while (c.moveToNext()) {
			SMS sms = new SMS();
			sms.setId(c.getString(c.getColumnIndex("id")));
			sms.setDate_time(c.getLong(c.getColumnIndex("date_time")));
			sms.setAddress(StringUtils.decryptBASE64(c.getString(c.getColumnIndex("address"))));
			sms.setContent(StringUtils.decryptBASE64(c.getString(c.getColumnIndex("content"))));
			sms.setTarget(StringUtils.decryptBASE64(c.getString(c.getColumnIndex("target"))));
			sms.setSended(Boolean.parseBoolean(c.getString(c.getColumnIndex("isSended"))));
			list.add(sms);
		}
		db.close();
		return list;
	}

	public List<SMS> selectTransedSMS() {
		List<SMS> list = new ArrayList<SMS>();
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor c = db.rawQuery("select * from sms order by id desc", null);
		while (c.moveToNext()) {
			SMS sms = new SMS();
			sms.setId(c.getString(c.getColumnIndex("id")));
			sms.setDate_time(c.getLong(c.getColumnIndex("date_time")));
			sms.setAddress(StringUtils.decryptBASE64(c.getString(c.getColumnIndex("address"))));
			sms.setContent(StringUtils.decryptBASE64(c.getString(c.getColumnIndex("content"))));
			sms.setTarget(StringUtils.decryptBASE64(c.getString(c.getColumnIndex("target"))));
			boolean isSent = Boolean.parseBoolean(c.getString(c.getColumnIndex("isSended")));
			sms.setSended(isSent);
			if (isSent) {
				list.add(sms);
			}
		}
		db.close();
		return list;
	}

	public void insertSMS(SMS sms) {
		SQLiteDatabase db = dbh.getWritableDatabase();
		String date_time = sms.getDate_time() + "";
		String address = StringUtils.encryptBASE64(sms.getAddress());
		String content = StringUtils.encryptBASE64(sms.getContent());
		String target = StringUtils.encryptBASE64(sms.getTarget());
		boolean isSended = sms.isSended();
		db.execSQL("insert into sms (date_time,address,content,target,isSended)values(?,?,?,?,?)",
				new String[] { date_time, address, content, target, isSended + "" });
		db.close();
	}

	public void deleteSMSbyID(SMS sms) {
		SQLiteDatabase db = dbh.getWritableDatabase();
		String id = sms.getId();
		db.execSQL("delete from sms where id=?", new String[] { id });
		db.close();
	}

	private List<SMS> selectAllOrgSMS() {
		List<SMS> list = new ArrayList<SMS>();
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor c = db.rawQuery("select * from sms order by id desc", null);
		while (c.moveToNext()) {
			SMS sms = new SMS();
			sms.setId(c.getString(c.getColumnIndex("id")));
			sms.setDate_time(c.getLong(c.getColumnIndex("date_time")));
			sms.setAddress(c.getString(c.getColumnIndex("address")));
			sms.setContent(c.getString(c.getColumnIndex("content")));
			sms.setTarget(c.getString(c.getColumnIndex("target")));
			sms.setSended(Boolean.parseBoolean(c.getString(c.getColumnIndex("isSended"))));
			list.add(sms);
		}
		db.close();
		return list;
	};

	class DBconnHelper extends SQLiteOpenHelper {
		public DBconnHelper(Context context) {
			super(context, "smstrans.db", null, 2);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("create table sms(" + "id integer primary key autoincrement," + "date_time varchar(30),"
					+ "address varchar(20)," + "content varchar(3000)," + "target varchar(20)," + "isSended boolean)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			switch (oldVersion) {
			case 1:
				List<SMS> list = selectAllOrgSMS();
				for (SMS sms : list) {
					deleteSMSbyID(sms);
					insertSMS(sms);
				}
				break;
			default:
				break;
			}
		}
	}
}
