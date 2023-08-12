DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE hits (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(255)                           NOT NULL,
    uri VARCHAR(512)                           NOT NULL,
    ip VARCHAR(20)                             NOT NULL,
    hit_time TIMESTAMP,
    CONSTRAINT pk_hits PRIMARY KEY (id)
);