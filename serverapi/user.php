<?php
// объект 'user' 
class User {
 
    // подключение к БД таблице "users" 
    private $conn;
    private $table_name = "users";
   private $table_name2 = "usertest";
    // свойства объекта 
    public $id;
    public $login;
    public $password;
    public $email;
    public $gender;
    public $role;
public $test;
    // конструктор класса User 
    public function __construct($db) {
        $this->conn = $db;
    }

    // Создание нового пользователя 
    function create() {
    
        // Вставляем запрос 
        $query = "INSERT INTO " . $this->table_name . "
                SET
                    login = :login,
                    gender = :gender,
                    email = :email,
                    password = :password,
		    role = :role";
    
        // подготовка запроса 
        $stmt = $this->conn->prepare($query);
    
        // инъекция 
        $this->login=htmlspecialchars(strip_tags($this->login));
	$this->password=htmlspecialchars(strip_tags($this->password));
	$this->gender=htmlspecialchars(strip_tags($this->gender));
	$this->email=htmlspecialchars(strip_tags($this->email));
	$this->role=htmlspecialchars(strip_tags($this->role));
        // привязываем значения 
        $stmt->bindParam(':login', $this->login);
        $stmt->bindParam(':gender', $this->gender);
        $stmt->bindParam(':email', $this->email);
	$stmt->bindParam(':role', $this->role);
    
        // для защиты пароля 
        // хешируем пароль перед сохранением в базу данных 
        $password_hash = password_hash($this->password, PASSWORD_BCRYPT);
        $stmt->bindParam(':password', $this->password);
    
        // Выполняем запрос 
        // Если выполнение успешно, то информация о пользователе будет сохранена в базе данных 
        
    if (!$this->loginExists()){
	if($stmt->execute()) {
            return $this->conn->lastInsertId();
        }
    	}
        return 0;
    }

    // Проверка, существует ли электронная почта в нашей базе данных 
function userExists(){
 
    // запрос, чтобы проверить, существует ли электронная почта 
    $query = "SELECT *
            FROM " . $this->table_name . "
            WHERE login = ? And password = ?
            LIMIT 0,1";
 
    // подготовка запроса 
    $stmt = $this->conn->prepare( $query );
 
    // инъекция 
    $this->login=htmlspecialchars(strip_tags($this->login));
 $this->password=htmlspecialchars(strip_tags($this->password));
    // привязываем значение e-mail 
    $stmt->bindParam(1, $this->login);
  	$stmt->bindParam(2, $this->password);
    // выполняем запрос 
    $stmt->execute();
 
    // получаем количество строк 
    $num = $stmt->rowCount();
 
    // если электронная почта существует, 
    // присвоим значения свойствам объекта для легкого доступа и использования для php сессий 
    if($num>0) {
 
        // получаем значения 
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
 
        // присвоим значения свойствам объекта 
        $this->id = $row['id'];
        $this->login = $row['login'];
        $this->email = $row['email'];
        $this->password = $row['password'];
        $this->gender = $row['gender'];
	$this->role = $row['role'];
        // вернём 'true', потому что в базе данных существует электронная почта 
        return true;
    }
 
    // вернём 'false', если адрес электронной почты не существует в базе данных 
    return false;
}
 
// здесь будет метод update()

function loginExists(){
 
    // запрос, чтобы проверить, существует ли электронная почта 
    $query = "SELECT *
            FROM " . $this->table_name . "
            WHERE login = ? 
            LIMIT 0,1";
 
    // подготовка запроса 
    $stmt = $this->conn->prepare( $query );
 
    // инъекция 
    $this->login=htmlspecialchars(strip_tags($this->login));
 $this->password=htmlspecialchars(strip_tags($this->password));
    // привязываем значение e-mail 
    $stmt->bindParam(1, $this->login);
  	
    // выполняем запрос 
    $stmt->execute();
 
    // получаем количество строк 
    $num = $stmt->rowCount();
 
    // если электронная почта существует, 
    // присвоим значения свойствам объекта для легкого доступа и использования для php сессий 
    if($num>0) {
 
        // получаем значения 
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
 
        // присвоим значения свойствам объекта 
        $this->id = $row['id'];
        $this->login = $row['login'];
        $this->email = $row['email'];
        $this->password = $row['password'];
        $this->gender = $row['gender'];
	$this->role = $row['role'];
        // вернём 'true', потому что в базе данных существует электронная почта 
        return true;
    }
       return false;
}
 // обновить запись пользователя 
function update(){
 
    // Если в HTML-форме был введен пароль (необходимо обновить пароль) 
    $password_set=!empty($this->password) ? ", password = :password" : "";
 
    // если не введен пароль - не обновлять пароль 
    $query = "UPDATE " . $this->table_name . "
            SET
               	    login = :login,
                    gender = :gender,
                    email = :email,
                    password = :password
		            WHERE id = :id";
 
    // подготовка запроса 
    $stmt = $this->conn->prepare($query);
 
    // инъекция (очистка) 
        $this->login=htmlspecialchars(strip_tags($this->login));
	$this->password=htmlspecialchars(strip_tags($this->password));
	$this->gender=htmlspecialchars(strip_tags($this->gender));
	$this->email=htmlspecialchars(strip_tags($this->email));
	
        // привязываем значения 
        $stmt->bindParam(':login', $this->login);
        $stmt->bindParam(':gender', $this->gender);
        $stmt->bindParam(':email', $this->email);
	
    
        // для защиты пароля 
        // хешируем пароль перед сохранением в базу данных 
        $password_hash = password_hash($this->password, PASSWORD_BCRYPT);
        $stmt->bindParam(':password', $this->password);
 
    // метод password_hash () для защиты пароля пользователя в базе данных 
    //if(!empty($this->password)){
     //   $this->password=htmlspecialchars(strip_tags($this->password));
      //  $password_hash = password_hash($this->password, PASSWORD_BCRYPT);
       // $stmt->bindParam(':password', $password_hash);
    //}
 
    // уникальный идентификатор записи для редактирования 
    $stmt->bindParam(':id', $this->id);
 
    // Если выполнение успешно, то информация о пользователе будет сохранена в базе данных 
   
	if($stmt->execute()) {
        return true;
    
 }
    return false;
}
    // вернём 'false', если адрес электронной почты не существует в базе данных 
 
 function users(){
 
    // запрос, чтобы проверить, существует ли электронная почта 
    $query = "SELECT *
            FROM " . $this->table_name;
 
    // подготовка запроса 
    $stmt = $this->conn->prepare( $query );
	
    
    // выполняем запрос 
    $stmt->execute();
 
    // получаем количество строк 
    $num = $stmt->rowCount();
    

    // если электронная почта существует, 
    // присвоим значения свойствам объекта для легкого доступа и использования для php сессий 
    if($num>0) {
 
        // получаем значения 
$row = $stmt->fetchAll(PDO::FETCH_ASSOC);
$myArray = array();
for ($index = 0; $index < count($row); $index++)
{
	
        $this->id = $row[$index]['id'];
         $this->gender = $row[$index]['gender'];
         $this->login = $row[$index]['login'];
         $this->password = $row[$index]['password'];
 
        // присвоим значения свойствам объекта 
         array_push($myArray, ['id' =>($this->id),'gender' =>($this->gender),'login' =>($this->login),'password' =>($this->password)]);
         
}
	
        // вернём 'true', потому что в базе данных существует электронная почта 
        return $myArray;
    }
 
    // вернём 'false', если адрес электронной почты не существует в базе данных 
    return 0;
}
function all_users(){
 
    // запрос, чтобы проверить, существует ли электронная почта 
    $query = "SELECT *, count(test_id) as result
            FROM " . $this->table_name.",".$this->table_name2." WHERE users.id=user_id group by users.id";
 
    // подготовка запроса 
    $stmt = $this->conn->prepare( $query );
   //$this->id=new User($db);
    // инъекция 
    // выполняем запрос 
    $stmt->execute();
 
    // получаем количество строк 
    $num = $stmt->rowCount();
    

    // если электронная почта существует, 
    // присвоим значения свойствам объекта для легкого доступа и использования для php сессий 
    if($num>0) {
 
        // получаем значения 
$row = $stmt->fetchAll(PDO::FETCH_ASSOC);
$myArray = array();
for ($index = 0; $index < count($row); $index++)
{
        $this->id = $row[$index]['id'];
$this->login = $row[$index]['login'];
$this->password = $row[$index]['password'];
$this->email = $row[$index]['email'];
$this->gender = $row[$index]['gender'];
$this->result = $row[$index]['result'];
      array_push($myArray, ['id' =>($this->id),'login' =>($this->login),'password' =>($this->password),'email' =>($this->email),'gender' =>($this->gender),'result' =>($this->result)]);
 		
}
        // вернём 'true', потому что в базе данных существует электронная почта 
        return $myArray;
    }
 
    // вернём 'false', если адрес электронной почты не существует в базе данных 
    return 0;
}
 
}// здесь будет метод update()
