package fr.eseo.e5e.ag.whatcolour.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.view.View
import androidx.core.content.ContextCompat
import fr.eseo.e5e.ag.whatcolour.R
import fr.eseo.e5e.ag.whatcolour.model.Colours

class ColorTextView(context: Context?) : View(context) {
    var colour: Int = 0
    var name: Int = 0
    var paint : TextPaint = TextPaint()

    constructor(context: Context?, colour: Int, name: Int) : this(context) {
        this.colour = colour
        this.name = name
    }

    constructor(context: Context?, colour: Int, name: Int, paint: TextPaint) : this(context) {
        this.colour = colour
        this.name = name
        this.paint = paint
    }

    init {
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_grey))
        paint = TextPaint()
        paint.isAntiAlias = true
        paint.textSize = 64* context!!.resources.displayMetrics.density
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true
        invalidate()
    }

    fun setColour(indiceColour: Int, indiceName: Int) {
        colour = indiceColour
        name = indiceName
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.save()
        canvas?.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        val colourName = context.resources.getString(Colours.stringReferences[name])
        val colour = ContextCompat.getColor(context, Colours.colourReferences[colour])
        paint.color = colour
        val xPos = canvas?.width?.div(2)?.toFloat() ?:0F
        val yPos = (canvas?.height?.div(2)?.minus((paint.descent() + paint.ascent()) / 2)) ?:0F
        canvas?.drawText(colourName, xPos, yPos, paint)
        canvas?.restore()
    }
}