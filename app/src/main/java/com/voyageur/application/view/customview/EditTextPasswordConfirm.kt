package com.voyageur.application.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import com.voyageur.application.R

class EditTextPasswordConfirm : AppCompatEditText, View.OnFocusChangeListener {
    var isPasswordValid = false

    init {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        transformationMethod = PasswordTransformationMethod.getInstance()
        onFocusChangeListener = this

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            validatePassword()
        }
    }

    private fun validatePassword() {
        val password = text.toString().trim()
        val confirmPassword =
            (parent as ViewGroup).findViewById<EditTextPassword>(R.id.etPassword).text.toString().trim()
        isPasswordValid = password.length >= 8 && password == confirmPassword
        error = if (!isPasswordValid) {
            resources.getString(R.string.password_tidak_sama)
        } else {
            null
        }
    }
}
