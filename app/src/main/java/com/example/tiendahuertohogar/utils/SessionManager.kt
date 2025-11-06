package com.example.tiendahuertohogar.utils


import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREFS_NAME = "LoginPrefs"
    private const val KEY_USERNAME = "username"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_PHOTO_URI = "photo_uri"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Guarda el nombre de usuario en SharedPreferences
    fun saveSession(context: Context, username: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    // Obtiene el nombre de usuario guardado. Devuelve null si no hay sesión.
    fun getSession(context: Context): String? {
        return getPreferences(context).getString(KEY_USERNAME, null)
    }

    // Limpia la sesión
    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }

    // Guarda el URI de la foto de perfil
    fun savePhotoUri(context: Context, uri: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_PHOTO_URI, uri)
        editor.apply()
    }

    // Obtiene el URI de la foto de perfil
    fun getPhotoUri(context: Context): String? {
        return getPreferences(context).getString(KEY_PHOTO_URI, null)
    }

    // Limpia el URI de la foto de perfil
    fun clearPhotoUri(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(KEY_PHOTO_URI)
        editor.apply()
    }

    // Guarda el correo del usuario en SharedPreferences
    fun saveUserEmail(context: Context, email: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_USER_EMAIL, email)
        editor.apply()
    }

    // Obtiene el correo del usuario guardado. Devuelve null si no hay sesión.
    fun getUserEmail(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_EMAIL, null)
    }
}
