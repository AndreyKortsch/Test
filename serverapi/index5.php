<?php
$link = mysqli_connect('localhost','root','12581258','test');
if(mysqli_connect_errno()) die('Ошибка соединения:'.mysqli_connect_error());
else {
if (isset($_GET['a'])) $string_a = htmlentities($_GET['a']);
$isEmpty = false;
if (empty($string_a)) $isEmpty = true;
if (!$isEmpty) {
$res = mysqli_query($link,"SELECT * FROM `answer` WHERE id = ".$string_a);
$row = mysqli_fetch_assoc($res);
if($res) {
echo $row['text'];
mysqli_free_result($res); 
}
mysqli_close($link);
}
else {
echo "error";
}
}
?>