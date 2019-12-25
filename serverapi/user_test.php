<?php
// требуемые заголовки 
header("Access-Control-Allow-Origin: http://authentication-jwt/");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
// подключение к БД 
// файлы, необходимые для подключения к базе данных 
include_once 'database.php';
include_once 'test.php';
include_once 'user.php'; 
// получаем соединение с базой данных 
$database = new Database();
$db = $database->getConnection();
 
// создание объекта 'User' 
$test = new Test($db);
$test->id=new User($db);
$test->id->login=$_GET['login'];
$test->id->password=$_GET['password'];
// устанавливаем значения 


$array=$test->userTest();

// здесь будет метод create()
// создание пользователя 
if (
    $array!=0 &&
    !empty($_GET['login']) &&
    !empty($_GET['password']) 	
    
) {
    // устанавливаем код ответа 
    http_response_code(200);
 
    // покажем сообщение о том, что пользователь был создан 
    echo json_encode($array);
}
 
// сообщение, если не удаётся создать пользователя 
else {
 
    // устанавливаем код ответа 
    http_response_code(400);
 
    // покажем сообщение о том, что создать пользователя не удалось 
    echo json_encode(array("message" => "Тестов нет"));
}
?>