package net.sytes.kai_soft.letsbuyka

interface IUserContract {
    fun refreshError(newString: String? = null)
    fun clearET(logInB: Boolean = false, passwordB: Boolean = false,
                rePasswordB: Boolean = false)
    fun eMailHasSent()

}