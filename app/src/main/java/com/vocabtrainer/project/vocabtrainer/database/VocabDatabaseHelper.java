package com.vocabtrainer.project.vocabtrainer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by steffy on 08/12/2017.
 */

class VocabDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = VocabDatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "vocabtrainer.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;

    private static final String CREATE_CATEGORY = "create table category(" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT NOT NULL);";

    private static final String CREATE_WORD = "create table word(" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "german TEXT NOT NULL, english TEXT NOT NULL, " +
            "category INTEGER, " +
            "FOREIGN KEY(category) REFERENCES category(_id));";

    private static final String INSERT_CATEGORIY = "insert into category(name) values " +
            "('breakfast')," +
            "('simple conversation')," +
            "('shopping')," +
            "('traveling')," +
            "('other');";

    private static final String INSERT_WORD_CAT1 = "insert into word(german, english, category) values\n" +
            "      ('Frühstück', 'breakfast', 1),\n" +
            "      ('Semmel', 'roll', 1),\n" +
            "      ('Butter', 'butter', 1),\n" +
            "      ('Brot', 'bread', 1),\n" +
            "      ('Schinken', 'ham', 1),\n" +
            "      ('Speck', 'bacon', 1),\n" +
            "      ('Wurst, Würstchen', 'sausage', 1),\n" +
            "      ('Eier', 'eggs', 1),\n" +
            "      ('Käse', 'cheese', 1),\n" +
            "      ('Kakao', 'hot chocolate', 1),\n" +
            "      ('Kaffee', 'coffee', 1),\n" +
            "      ('Tee', 'tea', 1),\n" +
            "      ('Zucker', 'sugar', 1),\n" +
            "      ('Honig', 'honey', 1),\n" +
            "      ('Marmelade', 'jam', 1),\n" +
            "      ('Obst', 'fruits', 1),\n" +
            "      ('Erdbeere', 'strawberry', 1),\n" +
            "      ('Aprikose, Marille', 'apricot', 1);";

    private static final String INSERT_WORD_CAT2 =  "insert into word(german, english, category) values\n" +
            "      ('hallo, servus', 'hello', 2),\n" +
            "      ('Guten Tag.', 'Good day.', 2),\n" +
            "      ('Guten Morgen.', 'Good morning.', 2),\n" +
            "      ('Guten Abend.', 'Good evening.', 2),\n" +
            "      ('Gute Nacht.', 'Good night.', 2),\n" +
            "      ('Wie geht es dir? Wie geht es Ihnen?', 'How are you?', 2),\n" +
            "      ('Mir geht es gut.', 'I am fine', 2),\n" +
            "      ('Mir geht es nicht so gut.', 'I am not doing so well', 2),\n" +
            "      ('Was hast du heute noch vor?', 'What are you up to today?', 2),\n" +
            "      ('Ich heiße ..., Mein Name ist ...', 'I am ..., My name is ...', 2),\n" +
            "      ('Wie heißt du? Wie heißen Sie?', 'What is your name?', 2),\n" +
            "      ('Ich bin .... Jahre alt.', 'I am ... years old.', 2),\n" +
            "      ('Wie alt bist du?', 'How old are you?', 2),\n" +
            "      ('eins', 'one', 2),\n" +
            "      ('zwei', 'two', 2),\n" +
            "      ('drei', 'three', 2),\n" +
            "      ('vier', 'four', 2),\n" +
            "      ('fünf', 'five', 2),\n" +
            "      ('sechs', 'six', 2),\n" +
            "      ('sieben', 'seven', 2),\n" +
            "      ('acht', 'eight', 2),\n" +
            "      ('neun', 'nine', 2),\n" +
            "      ('zehn', 'ten', 2);";

    private static final String INSERT_WORD_CAT3 = "insert into word(german, english, category) values\n" +
            "      ('Kleidung, Gewand', 'clothes', 3),\n" +
            "      ('Shirt, Leiberl', 'shirt', 3),\n" +
            "      ('Jeans', 'jeans', 3),\n" +
            "      ('Hose', 'trousers', 3),\n" +
            "      ('Rock', 'skirt', 3),\n" +
            "      ('Kleid', 'dress', 3),\n" +
            "      ('Socken', 'socks', 3),\n" +
            "      ('Mir gefällt diese Farbe.', 'I like this color', 3),\n" +
            "      ('Gibt es das auch Größe ...?', 'Do you also have size ...?', 3),\n" +
            "      ('Schuhe', 'shoes', 3),\n" +
            "      ('Pullover', 'pullover, sweater', 3),\n" +
            "      ('Mantel', 'coat', 3),\n" +
            "      ('Unterwäsche', 'underwear', 3),\n" +
            "      ('Wie viel kostet das?', 'How much is it?', 3),\n" +
            "      ('Das ist zu teuer.', 'That is too expensive.', 3),\n" +
            "      ('Schmuck', 'jewellery', 3),\n" +
            "      ('Halskette', 'necklace', 3),\n" +
            "      ('Armband', 'bracelet', 3),\n" +
            "      ('Ring', 'ring', 3);";

    private static final String INSERT_WORD_CAT4 = "insert into word(german, english, category) values\n" +
            "      ('Flughafen', 'airport', 4),\n" +
            "      ('Bus, Autobus', 'bus', 4),\n" +
            "      ('Bim, Straßenbahn', 'tram, tramway', 4),\n" +
            "      ('U-Bahn', 'metro, underground', 4),\n" +
            "      ('Supermarkt', 'supermarket', 4),\n" +
            "      ('auf der rechten Seite', 'on the right side', 4),\n" +
            "      ('auf der linken Seite', 'on the left side', 4),\n" +
            "      ('um die Ecke', 'around the corner', 4),\n" +
            "      ('Straße', 'street', 4),\n" +
            "      ('geradeaus', 'straight ahead', 4),\n" +
            "      ('Ausgang', 'exit', 4),\n" +
            "      ('Eingang', 'entry', 4);";

    public VocabDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL(CREATE_CATEGORY);
        db.execSQL(CREATE_WORD);

        // insert values
        db.execSQL(INSERT_CATEGORIY);
        db.execSQL(INSERT_WORD_CAT1);
        db.execSQL(INSERT_WORD_CAT2);
        db.execSQL(INSERT_WORD_CAT3);
        db.execSQL(INSERT_WORD_CAT4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
