# language: ru

Функция: Добавление товара

  Предыстория:
    * пользователь подключен к БД

  @correct
  Структура сценария: Добавление товара
    * проверка таблица "Food" существует в БД
    * добавляем новый товар в БД
      | Имя          | <name>   |
      | Тип          | <type>   |
      | Экзотический | <exotic> |
    * проверка присутсвия товара в БД
      | Имя          | <name>   |
      | Тип          | <type>   |
      | Экзотический | <exotic> |
    * удаляем товар из БД
      | Имя          | <name>   |
      | Тип          | <type>   |
      | Экзотический | <exotic> |
    * проверка отсутсвия товара в БД
      | Имя          | <name>   |
      | Тип          | <type>   |
      | Экзотический | <exotic> |

    Примеры:
      | name   | type  | exotic |
      | Манго  | Фрукт | true   |
      | Огурец | Овощ  | false  |