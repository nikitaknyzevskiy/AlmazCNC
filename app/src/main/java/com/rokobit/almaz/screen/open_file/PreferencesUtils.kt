package com.rokobit.almaz.screen.open_file

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager
import java.util.*

object PreferencesUtils {
    private const val FLASH_VIDEO_PLAYED = "flash_video_played"
    private const val EXTENSION_BMP = ".bmp"
    private const val EXTENSION_PNG = ".png"
    private const val EXTENSION_JPG = ".jpg"
    private const val EXTENSION_JPEG = ".jpeg"
    private const val EXTENSION_PSD = ".psd"
    const val KEY_EXTENSION_BMP = "extension_bmp"
    const val KEY_EXTENSION_PNG = "extension_png"
    const val KEY_EXTENSION_JPG = "extension_jpg"
    const val KEY_EXTENSION_JPEG = "extension_jpeg"
    const val KEY_EXTENSION_PSD = "extension_psd"
    const val KEY_AP = "ap"
    const val KEY_STA = "sta"
    const val KEY_WIFI_BUTTON_LOCKED = "key_wifi_button_locked"
    const val KEY_ORIENTATION_AUTO = "orientation_auto"
    const val KEY_ORIENTATION_PORTRAIT = "orientation_portrait"
    const val KEY_ORIENTATION_LANDSCAPE = "orientation_landscape"
    const val KEY_SAVE_FORMAT_JPG = "save_format_jpg"
    const val KEY_SAVE_FORMAT_PNG = "save_format_png"
    const val KEY_SAVE_FORMAT_BMP = "save_format_bmp"
    const val COLOR_BLACK = "color_picker_black"
    const val COLOR_WHITE = "color_picker_white"
    const val COLOR_GRAY = "color_picker_gray"
    const val COLOR_YELLOW = "color_picker_yellow"
    const val COLOR_RED = "color_picker_red"
    const val COLOR_BLUE = "color_picker_blue"
    private const val DEFAULT_COLOR_BLACK = "#262626"
    private const val DEFAULT_COLOR_WHITE = "#FFFFFF"
    private const val DEFAULT_COLOR_GRAY = "#8D9a9C"
    private const val DEFAULT_COLOR_YELLOW = "#FFFF00"
    private const val DEFAULT_COLOR_RED = "#FF0000"
    private const val DEFAULT_COLOR_BLUE = "#0000FF"
    private const val ACTIVE_PICKER_NAME = "active_picker_name"
    private const val KEY_FILES_ARE_SAVED = "files_are_saved_flag"
    const val LOCK_SCREEN_ROTATION = "lock_screen_rotation"
    private const val LAST_PROJECT_NAME = "last_project_name"
    private const val APP_VERSION = "APP_VERSION"
    private fun getSharedPreferences(activity: Activity?): SharedPreferences? {
        return if (activity == null) null else PreferenceManager.getDefaultSharedPreferences(
            activity.applicationContext
        )
    }

    private fun getSharedPreferences(context: Context?): SharedPreferences? {
        return if (context == null) null else PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getActiveExtensions(activity: Activity?): List<String> {
        val preferences = getSharedPreferences(activity)
        val extensions: MutableList<String> =
            ArrayList()
        var flag = preferences!!.getBoolean(KEY_EXTENSION_BMP, false)
        if (flag) extensions.add(EXTENSION_BMP)
        flag = preferences.getBoolean(KEY_EXTENSION_PNG, false)
        if (flag) extensions.add(EXTENSION_PNG)
        flag = preferences.getBoolean(KEY_EXTENSION_JPG, false)
        if (flag) {
            extensions.add(EXTENSION_JPG)
            extensions.add(EXTENSION_JPEG)
        }
        flag = preferences.getBoolean(KEY_EXTENSION_PSD, false)
        if (flag) extensions.add(EXTENSION_PSD)
        if (extensions.isEmpty()) {
            extensions.add(EXTENSION_BMP)
            extensions.add(EXTENSION_PNG)
            extensions.add(EXTENSION_JPG)
            extensions.add(EXTENSION_JPEG)
            extensions.add(EXTENSION_PSD)
        }
        return extensions
    }

    val extensions: List<String>
        get() {
            val extensions: MutableList<String> =
                ArrayList()
            extensions.add(EXTENSION_BMP)
            extensions.add(EXTENSION_PNG)
            extensions.add(EXTENSION_JPG)
            extensions.add(EXTENSION_JPEG)
            extensions.add(EXTENSION_PSD)
            return extensions
        }

    fun saveSettingsParameter(
        name: String,
        flag: Boolean,
        activity: Activity?
    ) {
        if (activity == null) return
        val context = activity.applicationContext
        saveSettingsParameter(name, flag, context)
    }

    fun saveSettingsParameter(
        name: String,
        flag: Boolean,
        context: Context?
    ) {
        if (context == null) return
        val editor = getSharedPreferences(context)!!.edit()
        when (name) {
            KEY_ORIENTATION_AUTO, KEY_ORIENTATION_PORTRAIT, KEY_ORIENTATION_LANDSCAPE -> saveOrientationData(
                name,
                editor
            )
            KEY_SAVE_FORMAT_JPG, KEY_SAVE_FORMAT_PNG, KEY_SAVE_FORMAT_BMP -> saveFormatData(
                name,
                editor
            )
            KEY_EXTENSION_BMP, KEY_EXTENSION_PNG, KEY_EXTENSION_JPG, KEY_EXTENSION_PSD, KEY_AP, KEY_STA, KEY_WIFI_BUTTON_LOCKED -> editor.putBoolean(
                name,
                flag
            )
        }
        editor.apply()
    }

    fun setFilesSavingStatusFlag(flag: Boolean, activity: Activity?) {
        val editor =
            getSharedPreferences(activity)!!.edit()
        editor.putBoolean(KEY_FILES_ARE_SAVED, flag)
        editor.apply()
    }

    fun getFilesSavingStatus(activity: Activity?): Boolean {
        return activity != null && getSharedPreferences(activity)!!.getBoolean(KEY_FILES_ARE_SAVED, true)
    }

    private fun saveOrientationData(
        name: String,
        editor: SharedPreferences.Editor
    ) {
        val orientationMap =
            HashMap<String, Boolean>()
        orientationMap[KEY_ORIENTATION_AUTO] = name == KEY_ORIENTATION_AUTO
        orientationMap[KEY_ORIENTATION_PORTRAIT] = name == KEY_ORIENTATION_PORTRAIT
        orientationMap[KEY_ORIENTATION_LANDSCAPE] = name == KEY_ORIENTATION_LANDSCAPE
        for (key in orientationMap.keys) {
            editor.putBoolean(key, orientationMap[key]!!)
        }
    }

    private fun saveFormatData(name: String, editor: SharedPreferences.Editor) {
        val saveFormatMap =
            HashMap<String, Boolean>()
        saveFormatMap[KEY_SAVE_FORMAT_JPG] = name == KEY_SAVE_FORMAT_JPG
        saveFormatMap[KEY_SAVE_FORMAT_PNG] = name == KEY_SAVE_FORMAT_PNG
        saveFormatMap[KEY_SAVE_FORMAT_BMP] = name == KEY_SAVE_FORMAT_BMP
        for (key in saveFormatMap.keys) {
            editor.putBoolean(key, saveFormatMap[key]!!)
        }
    }

    fun getSavedSettingsParameter(name: String?, activity: Activity?): Boolean {
        return activity != null && getSharedPreferences(activity)!!.getBoolean(name, false)
    }

    fun getSavedSettingsParameter(
        name: String?,
        context: Context?
    ): Boolean {
        return getSharedPreferences(context)!!.getBoolean(name, false)
    }

    fun saveScreenRotationStateIsLocked(activity: Activity?, flag: Boolean) {
        if (activity == null) return
        val editor =
            getSharedPreferences(activity)!!.edit()
        editor.putBoolean(LOCK_SCREEN_ROTATION, flag)
        editor.apply()
    }

    fun getScreenRotationState(name: String?, activity: Activity?): Boolean {
        return activity != null && getSharedPreferences(activity)!!.getBoolean(name, false)
    }

    fun saveNewPickerColor(
        colorName: String?,
        color: String?,
        activity: Activity?
    ) {
        if (activity == null || colorName == null || color == null) return
        val editor =
            getSharedPreferences(activity)!!.edit()
        editor.putString(colorName, color)
        editor.apply()
    }

    fun getSavedPickerColor(colorName: String?, activity: Activity?): String {
        when (colorName) {
            COLOR_BLACK -> return if (activity == null) DEFAULT_COLOR_BLACK else getSharedPreferences(
                activity
            )!!.getString(colorName, DEFAULT_COLOR_BLACK)!!
            COLOR_WHITE -> return if (activity == null) DEFAULT_COLOR_WHITE else getSharedPreferences(
                activity
            )!!.getString(colorName, DEFAULT_COLOR_WHITE)!!
            COLOR_GRAY -> return if (activity == null) DEFAULT_COLOR_GRAY else getSharedPreferences(
                activity
            )!!.getString(colorName, DEFAULT_COLOR_GRAY)!!
            COLOR_YELLOW -> return if (activity == null) DEFAULT_COLOR_YELLOW else getSharedPreferences(
                activity
            )!!.getString(colorName, DEFAULT_COLOR_YELLOW)!!
            COLOR_RED -> return if (activity == null) DEFAULT_COLOR_RED else getSharedPreferences(
                activity
            )!!.getString(colorName, DEFAULT_COLOR_RED)!!
            COLOR_BLUE -> return if (activity == null) DEFAULT_COLOR_BLUE else getSharedPreferences(
                activity
            )!!.getString(colorName, DEFAULT_COLOR_BLUE)!!
        }
        return DEFAULT_COLOR_GRAY
    }

    fun saveActivePickerName(pickerName: String?, activity: Activity?) {
        if (activity == null || pickerName == null) return
        val editor =
            getSharedPreferences(activity)!!.edit()
        editor.putString(ACTIVE_PICKER_NAME, pickerName)
        editor.apply()
    }

    fun isColorPickerActive(colorName: String?, activity: Activity?): Boolean {
        return activity != null && colorName != null && getSharedPreferences(activity)!!.getString(ACTIVE_PICKER_NAME, "null") == colorName
    }

    fun getColorOfActivePicker(activity: Activity?): Int {
        if (activity == null) return Color.parseColor(DEFAULT_COLOR_GRAY)
        val pickerName = getSharedPreferences(activity)!!.getString(ACTIVE_PICKER_NAME, COLOR_GRAY)
        return Color.parseColor(
            getSavedPickerColor(
                pickerName,
                activity
            )
        )
    }

    fun setSplashVideoPlayed(activity: Activity?, flag: Boolean) {
        if (activity == null) return
        val editor =
            getSharedPreferences(activity)!!.edit()
        editor.putBoolean(FLASH_VIDEO_PLAYED, flag)
        editor.apply()
    }

    fun isSplashVideoPlayed(activity: Activity?): Boolean {
        return activity != null && getSharedPreferences(activity)!!.getBoolean(FLASH_VIDEO_PLAYED, false)
    }

    fun saveLastProjectName(name: String?, activity: Activity?) {
        if (activity == null) return
        val editor =
            getSharedPreferences(activity)!!.edit()
        editor.putString(LAST_PROJECT_NAME, name)
        editor.apply()
    }

    fun getLastProjectName(activity: Activity?): String? {
        return if (activity == null) null else getSharedPreferences(activity)!!.getString(LAST_PROJECT_NAME, "")
    }

    fun getAppVersion(activity: Activity?): String? {
        return getSharedPreferences(activity)!!.getString(APP_VERSION, null)
    }

    fun setAppVersion(activity: Activity?, currentBuildName: String?) {
        getSharedPreferences(activity)!!.edit()
            .putString(APP_VERSION, currentBuildName).apply()
    }
}

