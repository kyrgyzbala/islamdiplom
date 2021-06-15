package kg.programm.programmingapp.ui.login.login.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kg.programm.programmingapp.R

class GenericTextWatcher internal constructor(
    private val currentView: View,
    private val nextView: View?
) :
    TextWatcher {
    override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
        val text = editable.toString()
        when (currentView.id) {
            R.id.editText1 -> {
                if (text.length == 1) {
                    nextView!!.requestFocus()
                }
            }
            R.id.editText2 -> {
                if (text.length == 1) {
                    nextView!!.requestFocus()
                }
            }
            R.id.editText3 -> {
                if (text.length == 1) {
                    nextView!!.requestFocus()
                }
            }
            R.id.editText4 -> {
                if (text.length == 1) {
                    nextView!!.requestFocus()
                }
            }
            R.id.editText5 -> {
                if (text.length == 1) {
                    nextView!!.requestFocus()
                }
            }
            //You can use EditText4 same as above to hide the keyboard
        }
    }

    override fun beforeTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

    override fun onTextChanged(
        arg0: CharSequence,
        arg1: Int,
        arg2: Int,
        arg3: Int
    ) { // TODO Auto-generated method stub
    }

}