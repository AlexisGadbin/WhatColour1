package fr.eseo.e5e.ag.whatcolour.model

import fr.eseo.e5e.ag.whatcolour.R

object Colours {
    val colourReferences : Array<Int>
    val stringReferences : Array<Int>
    val size : Int

    init {
        colourReferences = arrayOf(
            R.color.black, R.color.blue,
            R.color.red, R.color.purple,
            R.color.green, R.color.cyan,
            R.color.yellow, R.color.white,
            R.color.brown, R.color.orange,
            R.color.pink, R.color.grey)

        stringReferences = arrayOf(
            R.string.black, R.string.blue,
            R.string.red, R.string.purple,
            R.string.green, R.string.cyan,
            R.string.yellow, R.string.white,
            R.string.brown, R.string.orange,
            R.string.pink, R.string.grey)
        size = stringReferences.size
    }

}