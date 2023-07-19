package ai.nivarthana.loginapp.model

enum class Gender {
    GENDER, MALE, FEMALE, OTHER;

    companion object {
        fun getGenders(): Array<String> {
            return values().map { it.name }.toTypedArray()
        }
    }
}