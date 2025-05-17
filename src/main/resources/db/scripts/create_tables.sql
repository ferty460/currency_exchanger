CREATE TABLE currencies(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    code        VARCHAR(3) NOT NULL UNIQUE,
    full_name   VARCHAR(128) NOT NULL,
    sign        VARCHAR(128) NOT NULL
);

CREATE TABLE exchange_rates(
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id    INTEGER NOT NULL,
    target_currency_id  INTEGER NOT NULL,
    rate                DECIMAL(6) NOT NULL,
    FOREIGN KEY (base_currency_id)    REFERENCES currencies(id),
    FOREIGN KEY (target_currency_id)  REFERENCES currencies(id)
);