package jp.co.quocard.book_management_system.controller

import jp.co.quocard.book_management_system.domain.Author
import jp.co.quocard.book_management_system.service.AuthorService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/authors")
class AuthorController(private val authorService: AuthorService) {

    @GetMapping
    fun getAllAuthors(): List<Author> = authorService.getAllAuthors()

    @GetMapping("/{id}")
    fun getAuthorById(@PathVariable id: Long): Author? = authorService.getAuthorById(id)

    @PostMapping
    fun createAuthor(@RequestBody author: Author): Author = authorService.createAuthor(author)

    @PutMapping("/{id}")
    fun updateAuthor(@PathVariable id: Long, @RequestBody author: Author) {
        authorService.updateAuthor(author.copy(id = id))
    }
}
