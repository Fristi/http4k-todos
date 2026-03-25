import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: Int = 0,
    val title: String,
    val done: Boolean = false
)

@Serializable
data class CreateTodoRequest(val title: String)

@Serializable
data class UpdateTodoRequest(val title: String? = null, val done: Boolean? = null)