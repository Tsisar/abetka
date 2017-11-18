package ua.tsisar.abetka.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ua.tsisar.abetka.game.GameResult;
import ua.tsisar.abetka.preference.LoadPreference;

public class DBHelper extends SQLiteOpenHelper {
    private final static String TAG = "myLogs";

    private final static String TABLE = "results";
    private static final String STARS = "stars";
    private static final String ANSWER = "answer";
    private static final String DATE = "date";
    private static final String LANGUAGE = "language";
    private LoadPreference loadPreference;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "statistics", null, 1);
        loadPreference = new LoadPreference(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate database");
        // создаем таблицу с полями
        db.execSQL("create table " + TABLE + " ("
                + "id integer primary key autoincrement,"
                + LANGUAGE + " text,"
                + ANSWER + " integer,"
                + STARS + " integer,"
                + DATE + " text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<GameResult> getHeightResults(){
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<GameResult> list = new ArrayList<>();

        // делаем запрос всех данных из таблицы TABLE, получаем Cursor
        Cursor cursor = database.query(TABLE, null, LANGUAGE+" = \'" +
                        loadPreference.getLanguage() + "\'", null, null,
                null, loadPreference.getOrderBy(), loadPreference.getStatisticsNumber()); // LIMIT

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (cursor.moveToFirst()) {
            do {
                list.add(new GameResult(
                        cursor.getInt(cursor.getColumnIndex(ANSWER)),
                        cursor.getInt(cursor.getColumnIndex(STARS)),
                        cursor.getString(cursor.getColumnIndex(DATE))));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "0 rows");
        }
        cursor.close();

        return  list;
    }

    public void deleteHeightResults(){
        this.getWritableDatabase().execSQL("delete from results where "+LANGUAGE+" = \'"
                + loadPreference.getLanguage() + "\';");
    }

    public void saveHeightResult(int answer, int star){
        String language = loadPreference.getLanguage();
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = this.getWritableDatabase();

        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm",
                new Locale(language)).format(Calendar.getInstance().getTime());

        cv.put(LANGUAGE, language);
        cv.put(ANSWER, answer);
        cv.put(STARS, star);
        cv.put(DATE, timeStamp);

        long rowID = db.insert("results", null, cv);
        Log.d("myLogs", "row inserted, ID = " + rowID);

        db.execSQL("delete from results where "+LANGUAGE+" = \'" + language +
                "\' and id not in (select id from results where "+LANGUAGE+" = \'" + language +
                "\' order by " + loadPreference.getOrderBy() +
                " limit " + loadPreference.getStatisticsNumber() + ");");
    }

}