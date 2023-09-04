# language: ru

@all, @iu
Функция: Добавление товара

  Предыстория:
    * пользователь открыл браузер Chrome
    * пользователь открыл страницу по адресу "http://localhost:8080/food"

  @correct
  Структура сценария: Добавление товара
    * пользователь нажимает на "Добавить"
    * форма добавления товара "Открылась"
    * заполняются поля товара
      | Имя          | <name>   |
      | Тип          | <type>   |
      | Экзотический | <exotic> |
    * значения полей заполнены значениями
      | Имя          | <name>   |
      | Тип          | <type>   |
      | Экзотический | <exotic> |
    * пользователь нажимает на "Сохранить"
    * форма добавления товара "Закрылась"
    * в таблице пользователь видит новый товар
      | Имя          | <name>   |
      | Тип          | <type>   |
      | Экзотический | <exotic> |

    Примеры:
      | name   | type  | exotic |
      | Манго  | Фрукт | true   |
      | Огурец | Овощ  | false  |
