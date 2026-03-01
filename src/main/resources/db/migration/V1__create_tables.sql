CREATE TABLE author (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE book (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL CHECK (price >= 0),
    status VARCHAR(20) NOT NULL -- PRE_PUBLISHED, PUBLISHED
);

CREATE TABLE book_author (
    book_id BIGINT REFERENCES book(id) ON DELETE CASCADE,
    author_id BIGINT REFERENCES author(id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, author_id)
);
