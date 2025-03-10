CREATE TABLE books
(
    id                uuid                   NOT NULL,
    author_first_name character varying(255) NOT NULL,
    author_last_name  character varying(255) NOT NULL,
    isbn              character varying(255) NOT NULL,
    price             numeric(10, 2),
    publisher_name    character varying(255) NOT NULL,
    status            character varying(255) NOT NULL,
    stock_quantity    integer                NOT NULL,
    title             character varying(255) NOT NULL,
    CONSTRAINT books_status_check CHECK (((status)::text = ANY
                                          ((ARRAY ['AVAILABLE'::character varying, 'OUT_OF_STOCK'::character varying, 'DISCONTINUED'::character varying])::text[])))
);

ALTER TABLE ONLY books
    ADD CONSTRAINT books_pkey PRIMARY KEY (id);

ALTER TABLE ONLY books
    ADD CONSTRAINT ukkibbepcitr0a3cpk3rfr7nihn UNIQUE (isbn);
