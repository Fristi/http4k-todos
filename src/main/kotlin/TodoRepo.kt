import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Todos : Table("todos") {
    val id    = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val done  = bool("done").default(false)
    override val primaryKey = PrimaryKey(id)
}

class TodoRepo(private val db: Database) {

    init {
        transaction(db) { SchemaUtils.create(Todos) }
    }

    fun findAll(): List<Todo> = transaction(db) {
        Todos.selectAll().map { it.toTodo() }
    }

    fun findById(id: Int): Todo? = transaction(db) {
        Todos.selectAll().where { Todos.id eq id }.singleOrNull()?.toTodo()
    }

    fun create(title: String): Todo = transaction(db) {
        val newId = Todos.insert {
            it[Todos.title] = title
        } get Todos.id
        Todo(id = newId, title = title)
    }

    fun update(id: Int, title: String?, done: Boolean?): Todo? = transaction(db) {
        val existing = findById(id) ?: return@transaction null
        Todos.update({ Todos.id eq id }) {
            title?.let { t -> it[Todos.title] = t }
            done?.let { d -> it[Todos.done] = d }
        }
        existing.copy(
            title = title ?: existing.title,
            done  = done  ?: existing.done
        )
    }

    fun delete(id: Int): Boolean = transaction(db) {
        Todos.deleteWhere { Todos.id eq id } > 0
    }

    private fun ResultRow.toTodo() = Todo(
        id    = this[Todos.id],
        title = this[Todos.title],
        done  = this[Todos.done]
    )
}