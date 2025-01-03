//package com.example.squery;
//
//
//import static com.example.squery.SQLiteDataAdapter.chatItems;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class DBHelper extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "MyChatsDB";
//    private static final int DATABASE_VERSION = 1;
//
//    // Таблица чатов
//    private static final String TABLE_CHATS = "chats";
//    private static final String KEY_ID = "id";
//    private static final String KEY_CHAT_NAME = "chat_name";
//
//    public DBHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CHATS_TABLE = "CREATE TABLE " + TABLE_CHATS + "("
//                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + KEY_CHAT_NAME + " TEXT" + ")";
//        db.execSQL(CREATE_CHATS_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);
//        onCreate(db);
//    }
//
//    // Добавление нового чата
//    public void addChat(String chatName) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_CHAT_NAME, chatName);
//
//        db.insert(TABLE_CHATS, null, values);
//        db.close();
//    }
//
//    // Получение всех чатов
//    public List<ChatItem> getAllChats() {
//        List<ChatItem> chatList = new ArrayList<>();
//
//        String selectQuery = "SELECT * FROM " + TABLE_CHATS;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                int id = Integer.parseInt(cursor.getString(0));
//                String chatName = cursor.getString(1);
//
//                ChatItem chat = new ChatItem(id, chatName);
//                chatList.add(chat);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        return chatList;
//    }
//
//    public boolean deleteChatByName(String chatName) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        int rowsAffected = db.delete(TABLE_CHATS, KEY_CHAT_NAME + "=?", new String[]{chatName});
//        db.close();
//        return rowsAffected > 0; // Возвращает true, если удаление прошло успешно
//    }
//
//
//    public boolean chatExists(String chatName) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT COUNT(*) FROM " + TABLE_CHATS + " WHERE " + KEY_CHAT_NAME + " = ?";
//        Cursor cursor = db.rawQuery(query, new String[]{chatName});
//        int count = 0;
//        if (cursor.moveToFirst()) {
//            count = cursor.getInt(0);
//        }
//        cursor.close();
//        db.close();
//        return count > 0; // Возвращает true, если чат существует
//    }
//}
//
