package fr.eseo.e5e.ag.whatcolour.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import fr.eseo.e5e.ag.whatcolour.R
import fr.eseo.e5e.ag.whatcolour.model.Colours

class ColorTextView : View {
    var colour: Int = 0
    var name : Int = 0
    var paint : TextPaint

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    init {
        setBackgroundColor(
            ContextCompat.getColor(getContext(),
                R.color.light_grey))
        paint = TextPaint()
        paint.isAntiAlias = true
        paint.textSize = 64*context.resources.displayMetrics.density
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true
        invalidate()
    }

    fun setColour(indiceCouleur : Int, indiceName : Int) {
        colour = indiceCouleur
        name = indiceName
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.save()
        canvas?.translate(paddingLeft.toFloat(),paddingTop.toFloat())
        val colourName =
            context.resources.getString(Colours.stringReferences[name])
        val colour =
            ContextCompat.getColor(context,Colours.colourReferences[colour])
        paint.color = colour
        val xPos = canvas?.width?.div(2)?.toFloat() ?:0F
        val yPos = canvas?.height?.div(2)
            ?.minus((paint.descent()+paint.ascent()).div(2))?.toFloat()?:0F
        canvas?.drawText(colourName,xPos,yPos,paint)
        canvas?.restore()
    }

}