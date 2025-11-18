Number Search Service
Сервис для поиска N-ного минимального числа в XLSX файлах.

🚀 Быстрый старт
1. Клонирование репозитория
```bash
git clone https://github.com/neverovnikita/xlsx-numbers-sorter
cd xlsx-numbers-sorter
```
2. Сборка проекта (пропуская тесты)
```bash
mvn clean package -DskipTests
```
3. Запуск приложения
```bash
java -jar target/xlsx-numbers-sorter-0.0.1-SNAPSHOT.jar
```
4. Проверка работы
Откройте в браузере: http://localhost:8080/swagger-ui.html

📋 Использование
Пример запроса в Swagger:

json
{

  "filePath": "C:/путь/к/файлу.xlsx", 
  
  "position": 3
  
}

Пример тестового файла test.xlsx:
text
| A  |
|----|
| 5  |
| 2  |
| 8  |
| 1  |
| 9  |
| 3  |

Ожидаемый результат для position=3: 3
