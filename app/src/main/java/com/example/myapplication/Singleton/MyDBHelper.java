package com.example.myapplication.Singleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.Model.QuizQuestionModel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {

    public MyDBHelper(@Nullable Context context) {
        super(context, "GAMES.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String QUESTION = "QUESTION";
        String OPTION_LIST = "OPTION_LIST";
        String ANSWER = "ANSWER";
        String statement = "CREATE TABLE IF NOT EXISTS QUESTIONS (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QUESTION + " TEXT, " +
                OPTION_LIST + " TEXT, " +
                ANSWER + " TEXT)";

        sqLiteDatabase.execSQL(statement);

        statement = "CREATE TABLE IF NOT EXISTS SCORES (ID INTEGER PRIMARY KEY AUTOINCREMENT, GAME TEXT, SCORE DOUBLE, DATE REAL)";

        sqLiteDatabase.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addQuizQuestion(QuizQuestionModel quizQuestionModel) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("QUESTION", quizQuestionModel.getQuestion());
        contentValues.put("OPTION_LIST", quizQuestionModel.getOptionList().toString());
        contentValues.put("ANSWER", quizQuestionModel.getAnswer());

        long question_table = database.insert("QUESTIONS", null, contentValues);

        database.close();
        return question_table != -1;
    }

    public List<QuizQuestionModel> getQuizQuestionModels() {
        List<QuizQuestionModel> list = new ArrayList<>();

        String query = "SELECT * FROM QUESTIONS;";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do {
                String question = cursor.getString(1);
                String optionList = cursor.getString(2);
                String answer = cursor.getString(3);
                String[] options = optionList.split(",");
                List<String> optionList_ = new ArrayList<>();
                for(String option : options) {
                    option = option.replace("[", "").replace("]","").trim();
                    Log.i("option", option);
                    optionList_.add(option);
                }
                QuizQuestionModel quizQuestionModel = new QuizQuestionModel(question, optionList_, answer);
                list.add(quizQuestionModel);

            } while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }

    public boolean addScore(double score, String GAME_TYPE) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        contentValues.put("GAME", GAME_TYPE);
        contentValues.put("SCORE", score);
        contentValues.put("DATE", timestamp.getTime());

        long question_table = database.insert("SCORES", null, contentValues);

        database.close();
        return question_table != -1;
    }

    public void loadData() {
        SQLiteDatabase database = this.getWritableDatabase();

        String statement = "INSERT INTO QUESTIONS (QUESTION, OPTION_LIST, ANSWER)" +
                "VALUES ('Who is the captain in the Pirates of the Caribbean?', "+
                "'[Will Turner, Elizabeth Swann, Hector Barbossa, Jack Sparrow]', " +
                "'Jack Sparrow')";
        database.execSQL(statement);

        statement = "INSERT INTO QUESTIONS (QUESTION, OPTION_LIST, ANSWER)" +
                "VALUES ('The code in The Matrix comes from what food recipes?', "+
                "'[Sushi recipes, Dumpling recipes, Stir-fry recipes, Pad thai recipes]', " +
                "'Sushi recipes')";
        database.execSQL(statement);

        statement = "INSERT INTO QUESTIONS (QUESTION, OPTION_LIST, ANSWER)" +
                "VALUES ('Who actually drew the sketch of Rose in Titanic?', "+
                "'[Leonardo DiCaprio, Billy Zane, James Cameron, Kathy Bates]', " +
                "'James Cameron')";
        database.execSQL(statement);

        statement = "INSERT INTO QUESTIONS (QUESTION, OPTION_LIST, ANSWER)" +
                "VALUES ('Where were The Lord of the Rings movies filmed?', "+
                "'[Ireland, Iceland, New Zealand, Australia]', " +
                "'New Zealand')";
        database.execSQL(statement);

        statement = "INSERT INTO QUESTIONS (QUESTION, OPTION_LIST, ANSWER)" +
                "VALUES ('Who did the cat in The Godfather belong to?', "+
                "'[Francis Ford Coppola, Diane Keaton, Al Pachino, No one—the cat was a stray]', " +
                "'No one—the cat was a stray')";
        database.execSQL(statement);

        statement = "INSERT INTO QUESTIONS (QUESTION, OPTION_LIST, ANSWER)" +
                "VALUES ('What item is in every Fight Club scene?', "+
                "'[A Coca-Cola can, A Starbucks cup, A Dunkin’ donut, A Pepsi bottle]', " +
                "'A Starbucks cup')";
        database.execSQL(statement);
    }

    public double getRecentScore(String GAME_TYPE) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT SCORE FROM SCORES WHERE GAME = '" + GAME_TYPE + "' ORDER BY DATE DESC LIMIT 1;";
        Cursor cursor = database.rawQuery(query, null);
        double score = 0;
        if(cursor.moveToFirst()) {
            score = cursor.getDouble(0);
        }
        cursor.close();
        database.close();
        return score;
    }

}
