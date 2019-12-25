<?php
// заголовки 
header("Access-Control-Allow-Origin: http://authentication-jwt/");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
 
// здесь будет соединение с БД
// файлы необходимые для соединения с БД 
include_once 'database.php';
include_once 'user.php';
 
// получаем соединение с базой данных 
$database = new Database();
$db = $database->getConnection();
 
// создание объекта 'User' 
$user = new User($db);
 
// получаем данные 
$data = json_decode(file_get_contents("php://input"));
 
// устанавливаем значения 
$user->login = $_POST['login'];
$user->password = $_POST['password'];
$email_exists = $user->userExists();
 
// подключение файлов jwt 
include_once 'core.php';
include_once 'libs/php-jwt-master/src/BeforeValidException.php';
include_once 'libs/php-jwt-master/src/ExpiredException.php';
include_once 'libs/php-jwt-master/src/SignatureInvalidException.php';
include_once 'libs/php-jwt-master/src/JWT.php';
use \Firebase\JWT\JWT;
 
// существует ли электронная почта и соответствует ли пароль тому, что находится в базе данных 
if ( $email_exists && (password_verify($_POST['password'], $user->password)| ($_POST['password']==$user->password))) {
 
    $token = array(
       "iss" => $iss,
       "aud" => $aud,
       "iat" => $iat,
       "nbf" => $nbf,
       "data" => array(
           "id" => $user->id,
           "login" => $user->login,
	   "password" => $user->password,
           "gender" => $user->gender,
           "email" => $user->email,
	   "role" => $user->role
       )
    );
 
    // код ответа 
    http_response_code(200);
 
    // создание jwt 
    $jwt = JWT::encode($token,  $_POST['uuid']);
    
echo json_encode(
       
        array(
            "message" =>"Успешная авторизация",
            "jwt" => $jwt
        )

    );
 
}
 
// Если электронная почта не существует или пароль не совпадает, 
// сообщим пользователю, что он не может войти в систему 
else {
 
  // код ответа 
  http_response_code(401);

  // сказать пользователю что войти не удалось 
  echo json_encode(array("message" => "Ошибка входа."));
}
?>