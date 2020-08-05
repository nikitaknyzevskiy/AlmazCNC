package com.rokobit.almaz.screen.open_file

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.rokobit.almaz.R
import java.util.*

class AppTextAndKeyboardUtils {

    private var mEditTextList: MutableList<EditText>? = null
    private var mFileNames: MutableList<String>? = null

    private fun showKeyboard(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View?) {
        if (view == null) return
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    fun deactivateEditText(
        editText: EditText?,
        viewList: List<View>?
    ) {
        if (editText == null) return
        hideKeyboard(editText)
        editText.isFocusable = false
        if (viewList == null) return
        clearAllFocuses(viewList)
    }

    fun activateEditText(editText: EditText?) {
        if (editText == null) return
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
        editText.setSelection(editText.text.toString().length)
        showKeyboard(editText)
    }

    private fun clearAllFocuses(viewList: List<View>?) {
        if (viewList == null) return
        mEditTextList = ArrayList()
        mFileNames = ArrayList()
        for (view in viewList) {
            val rootLinearLayout: ConstraintLayout =
                (view as ConstraintLayout).findViewById(R.id.item_save_file_container)
            val count = rootLinearLayout.childCount
            for (i in 0 until count) {
                val v = rootLinearLayout.getChildAt(i)
                if (v is EditText) {
                    mEditTextList?.add(v)
                    mFileNames?.add(v.text.toString())
                    v.setFocusable(false)
                }
            }
        }
    }

    fun formatFloatInLocaleString(number: Float, commaDigitCount: Int): String? {
        return String.format(
            Locale.getDefault(),
            "%." + commaDigitCount + "f",
            number
        )
    }
}

