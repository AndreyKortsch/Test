<?php
// объект 'user' 
include_once 'user.php';
class Test {
 
    // подключение к БД таблице "users" 
    private $conn;
    private $table_name = "test";
    private $table_name2 = "usertest";
	private $table_name3 = "test_id";
	private $table_name4 = "users";
    // свойства объекта 
    public $test_id;
    public $id;
    public $date;
    public $answers;
     // конструктор класса User 
    public function __construct($db) {
        $this->conn = $db;
    }

    // Создание нового пользователя 
    function create() {
    
        // Вставляем запрос 
        $query = "INSERT INTO " . $this->table_name . "
                SET
                    date = NOW()";
    
        // подготовка запроса 
        $stmt = $this->conn->prepare($query);
    
      
        
    
	if($stmt->execute()) {
	
           return  $this->conn->lastInsertId();
        }
    	
        return 0;
    }
function createTest() {
    
        // Вставляем запрос 
        $query = "INSERT INTO " . $this->table_name2 . "
                SET
                    user_id = :user_id,
		    test_id=:test_id";
    
        // подготовка запроса 
        $stmt = $this->conn->prepare($query);
    
     	$stmt->bindParam(':user_id', $this->id);
        $stmt->bindParam(':test_id', $this->test_id);
        
    
	if($stmt->execute()) {
           return true;
        }
    	
        return false;
    }
function createAnswers($is) {
    
        // Вставляем запрос 
        $query = "INSERT INTO " . $this->table_name3 . "
                SET
                    id_test = :test_id,
		    id_answer=:answer,
		    antwort = :an";
    
        // подготовка запроса 
        $stmt = $this->conn->prepare($query);
    
      	$stmt->bindParam(':an', $this->answers);
        $stmt->bindParam(':test_id', $this->test_id);
        $stmt->bindParam(':answer', $is);
    
	if($stmt->execute()) {
           return true;
        }
    	
        return false;
    }
function userTest(){
 
    // запрос, чтобы проверить, существует ли электронная почта 
    $query = "SELECT *
            FROM " . $this->table_name2.",". $this->table_name4."
            WHERE user_id = users.id and login=? and password=?";
 
    // подготовка запроса 
    $stmt = $this->conn->prepare( $query );
	//$this->id=new User($db);
    // инъекция 
    $this->id->login=htmlspecialchars(strip_tags($this->id->login));
    $this->id->password=htmlspecialchars(strip_tags($this->id->password));
    // привязываем значение e-mail 
    $stmt->bindParam(1, $this->id->login);
  	$stmt->bindParam(2, $this->id->password);
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
        $this->test_id = $row[$index]['test_id'];
         
 		
$query = "SELECT *
            FROM " .$this->table_name3."
            WHERE id_test = ?";
 
    // подготовка запроса 
    $stmt = $this->conn->prepare( $query );
 
    // инъекция 
    //$this->id=htmlspecialchars(strip_tags($this->id));
    // привязываем значение e-mail 
    $stmt->bindParam(1, $this->test_id);
  	
    // выполняем запрос 
    $stmt->execute();
 
    // получаем количество строк 
    $num2 = $stmt->rowCount();
    
$this->answers="0";
    // если электронная почта существует, 
    // присвоим значения свойствам объекта для легкого доступа и использования для php сессий 
    if($num2>0) {
 $row2 = $stmt->fetchAll(PDO::FETCH_ASSOC);   	
 for ($index2 = 0; $index2 < count($row2); $index2++)
$this->answers=$this->answers.$row2[$index2]['antwort'];
}
        // присвоим значения свойствам объекта 
         array_push($myArray, ['test_id' =>($this->test_id),'answers' =>($this->answers)]);
         

	}
        // вернём 'true', потому что в базе данных существует электронная почта 
        return $myArray;
    }
 
    // вернём 'false', если адрес электронной почты не существует в базе данных 
    return 0;
}
 
}
