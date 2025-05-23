INSERT INTO currencies (code, full_name, sign)
VALUES ('USD', 'US Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('RUB', 'Russian ruble', '₽'),
       ('KZT', 'Tenge', '₸'),
       ('BYN', 'Belarusian ruble', 'Br'),
       ('JPY', 'Yen', '¥');

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
VALUES (1, 2, 0.96),
       (1, 3, 91.34),
       (1, 5, 3.26),
       (1, 6, 148.04),
       (4, 6, 0.3),
       (5, 4, 151.75),
       (5, 3, 28.25);