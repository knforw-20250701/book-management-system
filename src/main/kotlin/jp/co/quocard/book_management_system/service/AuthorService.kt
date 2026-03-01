package jp.co.quocard.book_management_system.service

import jp.co.quocard.book_management_system.domain.Author
import jp.co.quocard.book_management_system.repository.AuthorRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AuthorService(private val authorRepository: AuthorRepository) {

    fun getAllAuthors(): List<Author> = authorRepository.findAll()

    fun getAuthorById(id: Long): Author? = authorRepository.findById(id)

    fun createAuthor(author: Author): Author {
        validateAuthor(author)
        return authorRepository.save(author)
    }

    fun updateAuthor(author: Author) {
        validateAuthor(author)
        authorRepository.update(author)
    }

    private fun validateAuthor(author: Author) {
        if (author.birthDate.isAfter(LocalDate.now())) {
            throw IllegalArgumentException("生年月日は現在日以前である必要があります。")
        }
    }
}
