CRUD приложение + логи через аспекты
1. Создан простой RESTfull сервис для управления задачами:

1.2. POST /tasks - создание новой задачи.

1.3. GET /tasks/{id} - получение задачи по id

1.4. PUT /tasks/{id} - обновление задачи

1.5. DELETE /tasks/{id} - удаление задачи

1.6. GET /tasks - получение списка всех задач

2. Реализован класс аспект, со следующими advice: 

2.1 @Before

2.2 @AfterThrowing

2.3 @AfterReturning

2.4 @Around


3. В приложении реализована логика на каждый advice - свой метод.