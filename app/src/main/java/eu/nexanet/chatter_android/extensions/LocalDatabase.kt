package eu.nexanet.chatter_android.extensions

import android.annotation.SuppressLint
import android.content.Context

class LocalDatabase(
    val context: Context,
    val database: LocalDatabasePath
) {

    @SuppressLint("CommitPrefEdits")
    fun write(key: LocalDatabaseValuePath, data: String) {
        val editor = context.getSharedPreferences(database.path, Context.MODE_PRIVATE).edit()
        editor.putString(key.path, data)
        editor.apply()
    }

    fun read(key: LocalDatabaseValuePath): String? {
        return context.getSharedPreferences(database.path, Context.MODE_PRIVATE)
            .getString(key.path, null)
    }

    fun clear(key: LocalDatabaseValuePath) {
        val editor = context.getSharedPreferences(database.path, Context.MODE_PRIVATE).edit()
        editor.remove(key.path)
        editor.apply()
    }
}

enum class LocalDatabasePath(
    val path: String
) {
    DEVICE_DATA("device_data")
}

enum class LocalDatabaseValuePath(
    val path: String
) {
    NOTIFICATION_TOKEN("notification_token"),
    SESSION_TOKEN("session_token")
}