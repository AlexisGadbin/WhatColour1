package fr.eseo.e5e.ag.whatcolour.model

data class Result(
    val actualColour: String,
    val guessedColour: String
) {
    val isCorrect: Boolean
        get() = actualColour == guessedColour
}
