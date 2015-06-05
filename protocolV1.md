Описание API по курсам frontend/java
Структура API общая для всех методов:

API поддерживает GET и POST запросы
Адреса методов в формате /api/vНОМЕР_ВЕРСИИ_API/URL_МЕТОДА
Сервер всегда возвращает JSON с обязательными полями
status — Статус ответа, пересекается с HTTP статусами
body — Основное тело ответа, может быть пустым

    Общее для всех методов
    Если метод добавляет/изменяет/удаляет данные на сервере только POST (singup/score)
    Если метод требователен к безопасности только POST (signin/signout)
    Если метод только читает данные тогда GET
    Поле "status" пересекается с http статусами https://ru.wikipedia.org/wiki/Список_кодов_состояния_HTTP

    200 — OK
    400 — Bad Request — Ошибка запроса
    401 — Unauthorized — Необходима авторизация
    404 — Not Found — Данные не найдены
    405 — Method Not Allowed — Пришли с GET на метод требующий POST
    500 — Internal Server Error — Ошибка сервера
    501 — Not Implemented — Метод не реализован
    и т.д.

Обязательные методы
/api/v1/auth/signup — Регистрация
Принимает только POST запросы
Запрос: POST

    {

        name: String,

        email: String,

        password: String

    }

Ответ: 200

    {
        status: 200,

        body: {

            id: 2,

            name: "Vasya",

            email: "vasya@mail.ru",

            password: //last three symbols

            }

    }

/api/v1/auth/signin — Авторизация
Запрос: POST

    {

        login: String,

        password: String

    }

Ответ: 200

    {
        
        status: 200, 
        
        body: { 
        
            login: "Vasya" 
        
        } 
    
    }

/api/v1/auth/check — Проверка авторизации
Запрос: GET — без параметров
Ответ: 200

    { 
        status: 200, 
        body: { 
            id: 2, 
            name: "Vasya", 
            email: "vasya@mail.ru", 
            password: //last three symbols
            server: "12",
            role: "User",
            score: 1000
        } 
    }

Ответ: 401

    { 
    
        status: 401, 
    
        body: {message: "Unauthorised"} 
    
    }


/api/v1/auth/signout — Сброс авторизации
Запрос: POST — без параметров
Ответ: 200 / 401

    { 
        
        status: 200, 
        
        body: {} 
    
    }

/api/v1/scores — Игровая статистика
Запрос: GET

    {
    
        sort: {
    
            by: "date",
    
            order: "asc"
    
            }
    
    }

Ответ: 200 / 401

    {
        "data": 
        {
            "scoreList": 
                [
                {
                    "score": 14,
                        "login": "Vasya"
                },
                {
                    "score": 12,
                    "login": "Vanya"
                },
                {
                    "score": 10,
                    "login": "Petya"
                },
                {
                    "score": 2,
                    "login": "Danya"
                }
            ]
        },
        "status": 200
    }

    { 
        status: 401, 
        body: {message: "Unauthorised"} 
    }

Синхронизация

    {
    
        "status":"sync",
    
        "time":1000
    
        "firstPlayer":
    
            {
    
                "x":10.0,
    
                "y":11.0,
    
                "health":100,
    
                "position":1
    
            }
    
        "secondPlayer":
    
            {
    
                "x":10.0,
    
                "y":11.0,
    
                "health":100,
    
                "position":1
    
            }
    
    }

В случае получения урона (от клиента)

    {
    
        "fire":"success"
    
    }

Отправка настроек

    {
    
        "status":"settings",
    
        "map":[
    
                {"height":7,"values":[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]},
    
                {"height":6,"values":[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]},
    
                {"height":5,"values":[1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0]},
    
                {"height":4,"values":[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]},
    
                {"height":3,"values":[1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0]},
    
                {"height":2,"values":[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]},
    
                {"height":1,"values":[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]},
    
                {"height":0,"values":[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]}
    
            ]
    
    }


Начало игры

    {

        "status":"start",

        "position":1,       // варианты: 1 или 2
    
        "enemyName":"Qwerty",
    
        "sequence":"123456" // остатки от моей игры =)
    
    }

Игровое действие от клиента:

    {
        
        "action": 1,        // 1 - вверх, 2 - право, 3 - низ, 4 - влево, 5 - выстрел
        
        "x": 100,
        
        "y": 60
        
    }

Сообщение с телефона 

    {
    
        "action": 1,        // 1 - вверх, 2 - право, 3 - низ, 4 - влево
        
        "touchEvent": "touchStart"  //Варианты: "touchStart", "touchEnd"
    
    }
    
Игровое действие, которое отправляется Клиентам

    {
    
        "action": 1,        // 1 - вверх, 2 - право, 3 - низ, 4 - влево, 5 - выстрел
    
        "player": 1         // Соответсвует position игрока исполнившего это действие
    
    }
    
Ошибки
Код 1 - Противник прервал игру

    {
    
        "error":"message:
        
        "code": 1
    
    }

Конец игры

    {

        "status":"finish",

        "result":1,         // варианты: 0 - ничья, 1 - победил первый, 2 - победил второй;

    }