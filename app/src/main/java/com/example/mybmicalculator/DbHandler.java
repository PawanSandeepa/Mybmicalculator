package com.example.mybmicalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DbHandler extends SQLiteOpenHelper {
    private static final int version=1;
    private static final String db_name="bmi";
    private static final String table_name="bmi";

    //column names..........
    private static final String id="id";
    private static final String user_name="user_name";
    private static final String sex="sex";
    private static final String weight="weight";
    private static final String height="height";
    private static final String bmi="bmi";
    private static final String date="date";
    Context context;

    LineGraphSeries graphSeries;

    public DbHandler(@Nullable Context context) {
        super(context, db_name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table_create_quary="CREATE TABLE "+table_name+" ("
                +id+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +user_name+" TEXT,"
                +sex+" TEXT,"
                +weight+" TEXT,"
                +height+" TEXT,"
                +bmi+" TEXT,"
                +date+" TEXT"
                +");";
        db.execSQL(table_create_quary);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_table_quary="DROP TABLE IF EXISTS "+table_name;
        db.execSQL(drop_table_quary);
        onCreate(db);
    }

    public void add_bmi(model model){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(user_name,model.getUser_name());
        value.put(sex,model.getSex());
        value.put(weight,model.getWeight());
        value.put(height,model.getHeight());
        String sbmi = String.format("%.2f", model.getBmi());
        value.put(bmi,sbmi);

//        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//        String formattedDate = df.format(model.getDate());
        value.put(date,model.getDate());

        //-----------save to table--------------------------
        db.insert(table_name,null,value);
        Toasty.success(context,"recode adding success", Toast.LENGTH_LONG).show();
        db.close();
    }

    public LineData create_chart(){
        ArrayList<Entry> xy = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+table_name;
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Float getbmi= cursor.getFloat(5);
                Float getdate = cursor.getFloat(6);

                //Toast.makeText(context,""+getdate,Toast.LENGTH_LONG).show();

                xy.add(new Entry(getdate,getbmi));
            }while (cursor.moveToNext());
        }

        LineDataSet bmi = new LineDataSet(xy,"Your BMI");

        bmi.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(bmi);
        LineData data = new LineData(dataSets);


        return data;
    }

    public List<model> history_data(){
        List<model> data = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+table_name+" ORDER BY "+date+" DESC";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                model model = new model();

//                SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//                String formattedDate = df.format(cursor.getString(6));
                //long d = Long.parseLong(formattedDate);
                //long jnve=(cursor.getDate(6));
                model.setId(cursor.getString(0));
                model.setWeight(cursor.getDouble(3));
                model.setHeight(cursor.getDouble(4));
                model.setBmi(cursor.getDouble(5));
                model.setDate(cursor.getString(6));
                //model.setDate(d);

                //Toast.makeText(context,""+formattedDate,Toast.LENGTH_SHORT).show();

                data.add(model);
            }while (cursor.moveToNext());
        }

        return data;
    }

    public void delete(String get_id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table_name,id+" =?", new String[]{get_id});
        //db.execSQL("DELETE FROM bmi WHERE id="+get_id+";");

        Toast.makeText(context,"Recode was deleted",Toast.LENGTH_LONG).show();
        db.close();
    }
}
