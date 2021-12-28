package dev.armoury.android.widgets

import android.content.Context
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout

abstract class CustomTextInputLayout : TextInputLayout {

    constructor(context: Context) :
            super(context)
    constructor(context: Context, attributeSet: AttributeSet?) :
            super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) :
            super(context, attributeSet, defStyleAttr)

    init {
        initFont(context)
    }

    private fun initFont(context: Context) {
        val typeface = Typeface.createFromAsset(context.assets, getFontPath())

        val editText = editText
        if (editText != null) {
            editText.typeface = typeface
        }
        try {
            // Retrieve the CollapsingTextHelper Field
            val cthf = TextInputLayout::class.java.getDeclaredField("mCollapsingTextHelper")
            cthf.isAccessible = true

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            val cth = cthf.get(this)
            val tpf = cth.javaClass.getDeclaredField("mTextPaint")
            tpf.isAccessible = true

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            (tpf.get(cth) as TextPaint).typeface = typeface
        } catch (ignored: Exception) {
            // Nothing to do
        }

    }

    protected abstract fun getFontPath() : String

}