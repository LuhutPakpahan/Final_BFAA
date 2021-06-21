package robert.pakpahan.final_bfaa.source.network

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri
import robert.pakpahan.final_bfaa.source.database.AppLocalDatabase
import robert.pakpahan.final_bfaa.source.utils.FAVORITE_URI
import robert.pakpahan.final_bfaa.source.utils.URI_AUTHORITY
import robert.pakpahan.final_bfaa.source.utils.toFavoriteModel

class FavoriteUsersContenProvider : ContentProvider() {

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val favoriteDao = context?.let{ AppLocalDatabase.getDatabase(it).favoriteDao() }

        val count: Int = when(MATCHER.match(uri)){
            FAVORITE_ID -> {
                uri.lastPathSegment?.toInt()?.let {
                    favoriteDao?.deleteUser(it)
                } ?: 0
            } else -> 0
        }

        context?.contentResolver?.notifyChange(FAVORITE_URI.toUri(), null)

        return count
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val favoriteDao = context?.let { AppLocalDatabase.getDatabase(it).favoriteDao() }

        val id =  when (MATCHER.match(uri)){
            FAVORITE -> {
                values?.let {
                    favoriteDao?.insertUser(it.toFavoriteModel())
                } ?: 0
            }
            else -> 0
        }

        context?.contentResolver?.notifyChange(FAVORITE_URI.toUri(), null)

        return Uri.parse("$FAVORITE_URI/$id")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        val favoriteDao = context?.let { AppLocalDatabase.getDatabase(it).favoriteDao() }

        return when (MATCHER.match(uri)){
            FAVORITE -> favoriteDao?.selectAllUser()
            FAVORITE_ID -> uri.lastPathSegment?.toInt()?.let { favoriteDao?.selectDetailUser(it) }
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    companion object {
        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2

        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)

        var TAG = FavoriteUsersContenProvider::class.java.simpleName

        init {
            MATCHER.addURI(URI_AUTHORITY, "favorites_table", FAVORITE)
            MATCHER.addURI(URI_AUTHORITY, "favorites_table/#", FAVORITE_ID)
        }
    }

}
