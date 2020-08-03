package com.rokobit.almaz.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.rokobit.almaz.R

class CardBtnView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MaterialCardView(context, attrs, defStyleAttr) {

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        alpha = if (enabled)
            1f
        else
            0.5f
    }

    private val titleTextView: TextView

    private val imageView: ImageView

    var title: String = ""
        get() = titleTextView.text.toString()
        set(value) {
            field = value
            titleTextView.text = value
        }

    var icon: Drawable? = null
        get() = imageView.drawable
        set(value) {
            value?.apply {
                setTint(context.getColor(R.color.colorAccent))
            }
            field = value
            imageView.setImageDrawable(value)
        }

    init {
        setCardBackgroundColor(context.getColor(R.color.colorPrimary))
        strokeColor = context.getColor(R.color.colorPrimaryDark)
        strokeWidth = 1
        radius = 10f

        val contentView = LayoutInflater.from(context)
            .inflate(
                R.layout.view_card_btn,
                this,
                false
            )

        this.addView(contentView)

        titleTextView = contentView.findViewById(R.id.cardbtn_title)

        imageView = contentView.findViewById(R.id.cardbtn_image)

        val attributes = context.obtainStyledAttributes(
            attrs,
            R.styleable.CardBtnView,
            defStyleAttr,
            0
        )

        title = attributes.getString(R.styleable.CardBtnView_title)?:""


        icon = attributes.getDrawable(R.styleable.CardBtnView_icon)
    }

}