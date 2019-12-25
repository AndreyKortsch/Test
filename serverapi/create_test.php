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
$string=$_GET['result'];
$test->id=$_GET['id'];
// устанавливаем значения 
$test->test_id = $test->create();
for ($i=1;$i<=71;$i++){
$test->answers=(int)substr($string, $i, 1);
$test->createAnswers($i) ;
}
// здесь будет метод create()
// создание пользователя 
if (
    !empty($_GET['result']) &&
    !empty($_GET['id']) &&
    $test->createTest() &&
    (($test->test_id)>0)
) {
    // устанавливаем код ответа 
    http_response_code(200);
 
    // покажем сообщение о том, что пользователь был создан 
    echo json_encode(array("message" => $_GET['result']));
}
 
// сообщение, если не удаётся создать пользователя 
else {
 
    // устанавливаем код ответа 
    http_response_code(400);
 
    // покажем сообщение о том, что создать пользователя не удалось 
    echo json_encode(array("message" => $test->test_id));
}
?>