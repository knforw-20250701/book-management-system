package jp.co.quocard.book_management_system.repository

import jp.co.quocard.book_management_system.domain.Author
import jp.co.quocard.book_management_system.domain.Book
import jp.co.quocard.book_management_system.domain.BookStatus
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class BookRepository(private val dsl: DSLContext) {

    private val b = jp.co.quocard.bookmanagement.jooq.tables.Book.BOOK
    private val a = jp.co.quocard.bookmanagement.jooq.tables.Author.AUTHOR
    private val ba = jp.co.quocard.bookmanagement.jooq.tables.BookAuthor.BOOK_AUTHOR

    fun findAll(): List<Book> {
        val bookRecords = dsl.selectFrom(b).fetch()
        return bookRecords.map { r ->
            val id = r.get(b.ID)!!
            val authors = findAuthorsByBookId(id)
            Book(
                id = id,
                title = r.get(b.TITLE)!!,
                price = r.get(b.PRICE)!!,
                status = BookStatus.valueOf(r.get(b.STATUS)!!),
                authors = authors
            )
        }
    }

    fun findById(id: Long): Book? {
        val r = dsl.selectFrom(b).where(b.ID.eq(id)).fetchOne() ?: return null
        val authors = findAuthorsByBookId(r.get(b.ID)!!)
        return Book(
            id = r.get(b.ID),
            title = r.get(b.TITLE)!!,
            price = r.get(b.PRICE)!!,
            status = BookStatus.valueOf(r.get(b.STATUS)!!),
            authors = authors
        )
    }

    private fun findAuthorsByBookId(bookId: Long): List<Author> {
        return dsl.select(a.ID, a.NAME, a.BIRTH_DATE)
            .from(a)
            .join(ba).on(a.ID.eq(ba.AUTHOR_ID))
            .where(ba.BOOK_ID.eq(bookId))
            .fetch { r ->
                Author(
                    id = r.get(a.ID),
                    name = r.get(a.NAME)!!,
                    birthDate = r.get(a.BIRTH_DATE)!!
                )
            }
    }

    fun findByAuthorId(authorId: Long): List<Book> {
        return dsl.select(b.ID, b.TITLE, b.PRICE, b.STATUS)
            .from(b)
            .join(ba).on(b.ID.eq(ba.BOOK_ID))
            .where(ba.AUTHOR_ID.eq(authorId))
            .fetch { r ->
                val id = r.get(b.ID)!!
                Book(
                    id = id,
                    title = r.get(b.TITLE)!!,
                    price = r.get(b.PRICE)!!,
                    status = BookStatus.valueOf(r.get(b.STATUS)!!),
                    authors = emptyList()
                )
            }
    }

    fun save(book: Book): Book {
        val record = dsl.newRecord(b)
        record.set(b.TITLE, book.title)
        record.set(b.PRICE, book.price)
        record.set(b.STATUS, book.status.name)
        record.store()
        
        val bookId = record.get(b.ID)!!
        book.authors.forEach { author ->
            dsl.insertInto(ba)
                .set(ba.BOOK_ID, bookId)
                .set(ba.AUTHOR_ID, author.id)
                .execute()
        }
        
        return book.copy(id = bookId)
    }

    fun update(book: Book) {
        dsl.update(b)
            .set(b.TITLE, book.title)
            .set(b.PRICE, book.price)
            .set(b.STATUS, book.status.name)
            .where(b.ID.eq(book.id))
            .execute()
            
        dsl.deleteFrom(ba).where(ba.BOOK_ID.eq(book.id)).execute()
        book.authors.forEach { author ->
            dsl.insertInto(ba)
                .set(ba.BOOK_ID, book.id)
                .set(ba.AUTHOR_ID, author.id)
                .execute()
        }
    }
}
