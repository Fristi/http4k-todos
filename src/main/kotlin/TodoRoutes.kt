import org.http4k.contract.ContractRoute
import org.http4k.contract.RouteMetaDsl
import org.http4k.contract.div
import org.http4k.contract.meta
import org.http4k.core.*
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.lens.Path
import org.http4k.lens.int

object TodoRoutes {

    // Lenses
    private val todoLens         = Body.Companion.auto<Todo>().toLens()
    private val todoListLens     = Body.Companion.auto<List<Todo>>().toLens()
    private val createLens       = Body.Companion.auto<CreateTodoRequest>().toLens()
    private val updateLens       = Body.Companion.auto<UpdateTodoRequest>().toLens()
    private val idPath           = Path.int().of("id")

    fun routes(repo: TodoRepo): List<ContractRoute> = listOf(
        listTodos(repo),
        createTodo(repo),
        getTodo(repo),
        updateTodo(repo),
        deleteTodo(repo)
    )

    // GET /todos
    private fun listTodos(repo: TodoRepo) =
        "/todos" meta {
            summary     = "List all todos"
            returning(Status.Companion.OK, todoListLens to emptyList())
        } bindContract Method.GET to { _ ->
            todoListLens(repo.findAll(), Response.Companion(Status.Companion.OK))
        }

    // POST /todos
    private fun createTodo(repo: TodoRepo) =
        "/todos" meta {
            summary     = "Create a todo"
            receiving(createLens to CreateTodoRequest("Buy milk"))
            returning(Status.Companion.CREATED, todoLens to Todo(1, "Buy milk"))
        } bindContract Method.POST to { req ->
            val body = createLens(req)
            val todo = repo.create(body.title)
            todoLens(todo, Response.Companion(Status.Companion.CREATED))
        }

    // GET /todos/{id}
    private fun getTodo(repo: TodoRepo) =
        "/todos" / idPath meta {
            summary     = "Get a todo by ID"
            returning(Status.Companion.OK, todoLens to Todo(1, "Buy milk"))
            returning(Status.Companion.NOT_FOUND to "Todo not found")
        } bindContract Method.GET to { id ->
            { _ ->
                repo.findById(id)
                    ?.let { todoLens(it, Response.Companion(Status.Companion.OK)) }
                    ?: Response.Companion(Status.Companion.NOT_FOUND)
            }
        }

    // PATCH /todos/{id}
    private fun updateTodo(repo: TodoRepo) =
        "/todos" / idPath meta {
            summary     = "Update a todo"
            receiving(updateLens to UpdateTodoRequest(done = true))
            returning(Status.Companion.OK, todoLens to Todo(1, "Buy milk", done = true))
            returning(Status.Companion.NOT_FOUND to "Todo not found")
        } bindContract Method.PATCH to { id ->
            { req ->
                val body = updateLens(req)
                repo.update(id, body.title, body.done)
                    ?.let { todoLens(it, Response.Companion(Status.Companion.OK)) }
                    ?: Response.Companion(Status.Companion.NOT_FOUND)
            }
        }

    // DELETE /todos/{id}
    private fun deleteTodo(repo: TodoRepo) =
        "/todos" / idPath meta {
            summary = "Delete a todo"
            returning(Status.Companion.NO_CONTENT to "Deleted")
            returning(Status.Companion.NOT_FOUND to "Todo not found")
        } bindContract Method.DELETE to { id ->
            { _ ->
                if (repo.delete(id)) Response.Companion(Status.Companion.NO_CONTENT)
                else Response.Companion(Status.Companion.NOT_FOUND)
            }
        }
}