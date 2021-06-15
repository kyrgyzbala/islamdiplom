package kg.programm.programmingapp.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kg.programm.programmingapp.R
import java.text.SimpleDateFormat
import java.util.*


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

/**
 * Show the soft keyboard. On phones with a hard keyboard has the unfortunate side effect
 * of leaving the keyboard showing until we or the user dismiss it, even when going
 * to another application.
 */
fun View.showSoftKeyboard(force: Boolean = false) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (force) {
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}

fun Fragment.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(requireView(), message, duration).show()
}

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

fun View.showRegSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).setTextColor(Color.WHITE).show()
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

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun getDateToday(context: Context, date: String, type: Int): String {

    val calendar = Calendar.getInstance()
    val sdf = if (type == 1)
        SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    else
        SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val today = sdf.format(calendar.time)
    val yesterday = sdf.format(calendar.time.time - 86400000)

    return when {
        today == date -> context.getString(R.string.today)
        yesterday == date -> context.getString(R.string.yesterday)
        else -> getDateMonth(date, context, type)
    }
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

fun getTimeToShow(diff: Long, context: Context): String {
    val diffM = diff / 60
    val diffH = diffM / 60
    val diffD = diffH / 24
    val diffMonth = diffD / 30
    val diffYear = diffMonth / 12
    return when {
        diffYear > 0 -> "$diff ${context.getString(R.string.y)}"
        diffMonth > 0 -> "$diffMonth ${context.getString(R.string.m)}"
        diffD > 0 -> "$diffD ${context.getString(R.string.d)}"
        diffH > 0 -> "$diffH ${context.getString(R.string.h)}"
        diffM > 0 -> "$diffM ${context.getString(R.string.min)}"
        else -> "$diff ${context.getString(R.string.s)}"
    }
}