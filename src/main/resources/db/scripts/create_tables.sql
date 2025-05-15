CREATE TABLE currencies(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    code        VARCHAR(3) NOT NULL UNIQUE,
    fullName    VARCHAR(128) NOT NULL,
    sign        VARCHAR(128) NOT NULL
);

CREATE TABLE exchange_rates(
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    baseCurrencyId      INTEGER NOT NULL,
    targetCurrencyId    INTEGER NOT NULL,
    rate                DECIMAL(6) NOT NULL,
    FOREIGN KEY (baseCurrencyId)    REFERENCES Currencies (id),
    FOREIGN KEY (targetCurrencyId)  REFERENCES Currencies (id)
);