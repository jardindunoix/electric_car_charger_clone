package cl.codechunksdev.electriccarcharger2.domain.dto.sign_in

data class SignInPojo(
    val email: String,
    val password: String,
    val companyId: String,
)