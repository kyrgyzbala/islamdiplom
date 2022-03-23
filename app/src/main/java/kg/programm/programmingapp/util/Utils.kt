package kg.programm.programmingapp.util

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kg.programm.programmingapp.R

const val APP_LANG = "app_lang"
const val APP_LANG_KEY = "app_lang_key"

const val EXTRA_CODE_SENT = "EXTRA_CODE_SENT"
const val EXTRA_PHONE = "EXTRA_PHONE"
const val EXTRA_LEC_CAT = "EXTRA_LEC_CAT"

const val EXTRA_TIME_POS = "EXTRA_TIME_POS"
const val EXTRA_VIDEO_URL = "EXTRA_VIDEO_URL"

const val EXTRA_TASK = "extra_task"

const val EXTRA_TEST_REF = "EXTRA_TEST_REF"
const val EXTRA_TEST_NAME = "EXTRA_TEST_NAME"

const val EXTRA_QUESTIONS = "EXTRA_QUESTIONS"


inline var View.rightPaddingDp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, paddingRight.toFloat(),
        resources.displayMetrics
    )
    set(value) {
        val rightPx = resources.displayMetrics.density * value
        setPadding(paddingLeft, paddingTop, rightPx.toInt(), paddingBottom)
    }

inline var View.leftPaddingDp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, paddingLeft.toFloat(),
        resources.displayMetrics
    )
    set(value) {
        val leftPx = resources.displayMetrics.density * value
        setPadding(leftPx.toInt(), paddingTop, paddingRight, paddingBottom)
    }

fun TextView.hide() {
    visibility = View.GONE
}

fun TextView.show() {
    visibility = View.VISIBLE
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun RelativeLayout.show() {
    visibility = View.VISIBLE
}

fun RelativeLayout.hide() {
    visibility = View.GONE
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getDateMonth(date: String, context: Context, type: Int): String {

    return if (type == 1) {
        val year = date.takeLast(4)
        val day = date.take(2)
        val temp = date.take(5)
        val month = getMonth(context, temp.takeLast(2))
        "$day $month $year"
    } else {
        val year = date.take(4)
        val day = date.takeLast(2)
        val temp = date.takeLast(5)
        val month = getMonth(context, temp.take(2))
        "$day $month $year"
    }
}

fun getMonth(context: Context, month: String): String {
    return when (month) {
        "01" -> context.getString(R.string.jan)
        "02" -> context.getString(R.string.feb)
        "03" -> context.getString(R.string.march)
        "04" -> context.getString(R.string.apr)
        "05" -> context.getString(R.string.may)
        "06" -> context.getString(R.string.june)
        "07" -> context.getString(R.string.july)
        "08" -> context.getString(R.string.aug)
        "09" -> context.getString(R.string.sep)
        "10" -> context.getString(R.string.oct)
        "11" -> context.getString(R.string.nov)
        else -> context.getString(R.string.dec)
    }
}

