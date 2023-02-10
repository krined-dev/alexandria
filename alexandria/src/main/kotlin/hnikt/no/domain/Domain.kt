import io.ktor.client.request.*

@JvmInline
value class Version(val value: String)
@JvmInline
value class Code(val value: String)

@JvmInline
value class RegistryName(val value: String)

interface AlexandriaCode {
    val code: Code
    val description: String
    val version: Version
}

data class PostCode(
    val number: String,
    val county: String,
    val municipality: String,
    val city: String,
)

data class ICD10(override val code: Code, override val description: String, override val version: Version) : AlexandriaCode
data class NCMP(override val code: Code, override val description: String, override val version: Version) : AlexandriaCode
data class NCSP(override val code: Code, override val description: String, override val version: Version) : AlexandriaCode

data class ExternalHttpRequest(
    val request: String,
)

data class ExternalHttpResponse(
    val response: String,
)

enum class CodeTypes {
    POSTCODE, ICD10, NCMP, NCSP
}

data class CodeRequest(
    val codeType: CodeTypes,
    val version: Version,
    val fromRegistry: RegistryName
)

data class CodeResponse(
    val codes: List<AlexandriaCode>
)

interface InternalHttpClient {
    fun sendUpdatedDataNotification(updatedId: CodeTypes) : HttpRequest
}

interface Source {
    val url: String
    val name: String
    val code: CodeTypes
}

interface ExternalDsHttpClient {
    fun retrieveDataFromExternal(source: Source, request: ExternalHttpRequest): ExternalHttpResponse
}

interface StorageService {
    fun storeCodes(codes: List<AlexandriaCode>)
    fun retrieveCodes(codeType: CodeTypes, version: Version) : List<AlexandriaCode>

    fun updateCodes(codes: List<AlexandriaCode>)
}

interface RefreshService {
    fun runService()
}
