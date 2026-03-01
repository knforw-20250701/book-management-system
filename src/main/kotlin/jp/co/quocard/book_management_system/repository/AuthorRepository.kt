package jp.co.quocard.book_management_system.repository

import jp.co.quocard.book_management_system.domain.Author
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class AuthorRepository(private val dsl: DSLContext) {

    private val t = jp.co.quocard.bookmanagement.jooq.tables.Author.AUTHOR

    fun findAll(): List<Author> {
        return dsl.selectFrom(t)
            .fetch { r ->
                Author(
                    id = r.get(t.ID),
                    name = r.get(t.NAME)!!,
                    birthDate = r.get(t.BIRTH_DATE)!!
                )
            }
    }

    fun findById(id: Long): Author? {
        return dsl.selectFrom(t)
            .where(t.ID.eq(id))
            .fetchOne { r ->
                Author(
                    id = r.get(t.ID),
                    name = r.get(t.NAME)!!,
                    birthDate = r.get(t.BIRTH_DATE)!!
                )
            }
    }

    fun save(author: Author): Author {
        val record = dsl.newRecord(t)
        record.set(t.NAME, author.name)
        record.set(t.BIRTH_DATE, author.birthDate)
        record.store()
        return author.copy(id = record.get(t.ID))
    }
    
    fun update(author: Author) {
        dsl.update(t)
            .set(t.NAME, author.name)
            .set(t.BIRTH_DATE, author.birthDate)
            .where(t.ID.eq(author.id))
            .execute()
    }
}
