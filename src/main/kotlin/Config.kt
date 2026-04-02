import com.uchuhimo.konf.ConfigSpec


object DatabaseConfigSpec : ConfigSpec() {
    val url by optional("jdbc:h2:mem:todos;DB_CLOSE_DELAY=-1")
    val driver by optional("org.h2.Driver")
    val user by optional("sa")
    val password by optional("")
}