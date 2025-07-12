# 💱 Currency Exchanger (Обменник валют)

REST API для описания валют и обменных курсов. 
Позволяет просматривать и редактировать списки валют
и обменных курсов, и совершать расчёт конвертации 
произвольных сумм из одной валюты в другую.

## Стек технологий

- Java 17
- Servlet API (Jakarta EE 6)
- SQLite
- Tomcat 11
- Maven

## Установка

### Требования

- Java 17+
- Tomcat 11
- SQLite (база хранится в `.sqlite`-файле)

### Шаги установки

1. **Клонируйте репозиторий**

   ```bash
   git clone https://github.com/ferty460/currency_exchanger.git
   ```

2. **Настройте базу данных**

   - Создайте (или укажите путь к) пустому `.sqlite`-файлу на вашем компьютере.
   Например: `C:\data\currency.sqlite` или `./currency.sqlite`
   - Укажите путь к БД в файле src/main/resources/application.properties:

   ```properties
   db.url=jdbc:sqlite:/полный/путь/к/вашей/bd.sqlite
   ```

3. **Соберите проект**

   ```bash
   mvn clean package
   ```

   - После сборки в папке `target` появится файл `ROOT.war`

4. **Разместите приложение в Tomcat**

   - Скопируйте `ROOT.war` в папку `webapps` вашего Tomcat-сервера

5. Перезапустите Tomcat

   - После перезапуска приложение будет доступно по адресу `http://localhost:8080/`

## Документация API

**Базовый адрес:** `http://localhost:8080/` \
**Формат ответа:** JSON

### Получение валют

| **Метод** | **Путь**      | 
|-----------|---------------|
| GET       | `/currencies` |

#### Пример ответа

```json
[
  {
    "id": 1,
    "code": "USD",
    "name": "US Dollar",
    "sign": "$"
  },
  {
    "id": 2,
    "code": "EUR",
    "name": "Euro",
    "sign": "€"
  },
  {
    "id": 3,
    "code": "RUB",
    "name": "Russian ruble",
    "sign": "₽"
  }
]
```

---

### Получение валюты по коду

| **Метод** | **Путь**        | 
|-----------|-----------------|
| POST      | `/currency/USD` |

#### Пример ответа

```json
{
    "id": 1,
    "code": "USD",
    "name": "US Dollar",
    "sign": "$"
}
```

---

### Добавление валюты

| **Метод** | **Путь**      | **Тело запроса (`x-www-form-urlencoded`)**      | 
|-----------|---------------|-------------------------------------------------|
| POST      | `/currencies` | `code: USD`<br/>`name: US Dollar`<br/>`sign: $` |

#### Пример ответа

```json
{
    "id": 1,
    "code": "USD",
    "name": "US Dollar",
    "sign": "$"
}
```

---

### Получение всех курсов

| **Метод** | **Путь**         | 
|-----------|------------------|
| GET       | `/exchangeRates` |

#### Пример ответа

```json
[
  {
    "id": 1,
    "baseCurrency": {
      "id": 1,
      "code": "USD",
      "name": "US Dollar",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 2,
      "code": "EUR",
      "name": "Euro",
      "sign": "€"
    },
    "rate": 0.96
  },
  {
    "id": 2,
    "baseCurrency": {
      "id": 1,
      "code": "USD",
      "name": "US Dollar",
      "sign": "$"
    },
    "targetCurrency": {
      "id": 3,
      "code": "RUB",
      "name": "Russian ruble",
      "sign": "₽"
    },
    "rate": 91.34
  }
]
```

---

### Получение курса по валютам

| **Метод** | **Путь**               | 
|-----------|------------------------|
| GET       | `/exchangeRate/USDRUB` |

#### Пример ответа

```json
{
    "id": 2,
    "baseCurrency": {
        "id": 1,
        "code": "USD",
        "name": "US Dollar",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 3,
        "code": "RUB",
        "name": "Russian ruble",
        "sign": "₽"
    },
    "rate": 91.34
}
```

---

### Создание нового курса

| **Метод** | **Путь**         | **Тело запроса<br/>(`x-www-form-urlencoded`)**                         | 
|-----------|------------------|------------------------------------------------------------------------|
| POST      | `/exchangeRates` | `baseCurrencyCode: KZT`<br/>`targetCurrencyCode: RUB`<br/>`rate: 0.15` |

#### Пример ответа:

```json
{
    "id": 10,
    "baseCurrency": {
        "id": 4,
        "code": "KZT",
        "name": "Tenge",
        "sign": "₸"
    },
    "targetCurrency": {
        "id": 3,
        "code": "RUB",
        "name": "Russian ruble",
        "sign": "₽"
    },
    "rate": 0.15
}
```

---

### Обновление курса обмена

| **Метод** | **Путь**               | **Тело запроса<br/>(`x-www-form-urlencoded`)** | 
|-----------|------------------------|------------------------------------------------|
| PATCH     | `/exchangeRate/USDRUB` | `rate: 0.78`                                   |

#### Пример ответа

```json
{
    "id": 2,
    "baseCurrency": {
        "id": 1,
        "code": "USD",
        "name": "US Dollar",
        "sign": "$"
    },
    "targetCurrency": {
        "id": 3,
        "code": "RUB",
        "name": "Russian ruble",
        "sign": "₽"
    },
    "rate": 0.78
}
```

---

### Конвертация суммы

| **Метод** | **Путь**    | **Параметры**          | 
|-----------|-------------|------------------------|
| GET       | `/exchange` | `from`, `to`, `amount` |

#### Пример ответа

```json
{
  "baseCurrency": {
    "id": 4,
    "code": "KZT",
    "name": "Tenge",
    "sign": "₸"
  },
  "targetCurrency": {
    "id": 3,
    "code": "RUB",
    "name": "Russian ruble",
    "sign": "₽"
  },
  "rate": 0.15,
  "amount": 10.0,
  "convertedAmount": 1.5
}
```

**Весь функционал можно протестировать, 
импортировав файл `currency_exchanger.postman_collection.json` в ваш Postman**