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
include_once 'user.php';
 
// получаем соединение с базой данных 
$database = new Database();
$db = $database->getConnection();
 
// создание объекта 'User' 
$user = new User($db);
 
// отправляемые данные будут здесь
// получаем данные 
$data = json_decode(file_get_contents("php://input"));
 
// устанавливаем значения 
$user->login = $_POST['login'];
$user->password = $_POST['password'];
$user->email = $_POST['email'];
$user->gender = $_POST['gender'];
$user->role = $_POST['role'];
$user->id=$user->create();
// здесь будет метод create()
// создание пользователя 
if (
    !empty($user->login) &&
    !empty($user->password) &&
    !empty($user->email) &&
    ($user->id)>0	
) {
    // устанавливаем код ответа 
    http_response_code(200);
 
    // покажем сообщение о том, что пользователь был создан 
    echo json_encode(array("message" => "Пользователь был создан.","id" => $user->id,"login"=> $user->login,"password"=> $user->password));
}
 
// сообщение, если не удаётся создать пользователя 
else {
 
    // устанавливаем код ответа 
    http_response_code(400);
 
    // покажем сообщение о том, что создать пользователя не удалось 
    echo json_encode(array("message" => "Невозможно добавить пользователя."));
}
?>