<?php
$link = mysqli_connect('localhost','root','12581258','test');
if(mysqli_connect_errno()) die('Ошибка соединения:'.mysqli_connect_error());
else {
if (isset($_GET['a'])) $string_a = htmlentities($_GET['a']);
$isEmpty = false;
$u=0;
if (empty($string_a)) $isEmpty = true;
if (!$isEmpty) {
$res = mysqli_query($link,"SELECT * FROM `scals`");
$s1 = array_fill(1, mysqli_num_rows($res), "");
if($res) {
while($row = mysqli_fetch_assoc($res)) {
//echo $row['id']." ";
//echo $row['name']."<br>";
$u++;
$s1[$u]=$row['name'];
}
}
$s = array_fill(1, $u, 0);
for ($i=1;$i<=strlen($string_a);$i++){
$res = mysqli_query($link,"SELECT * FROM `scals`,`answer_scale`where answer_scale.scale_id=scals.id and answer_scale.answer_id=".$i);
if($res) {
while($row = mysqli_fetch_assoc($res)) {
//echo $row['answer_id']." ";
//echo $row['scale_id']." ";
//echo $row['answer']."<br>";
if ((int)substr($string_a, $i, 1)==$row['answer']) $s[$row['scale_id']]++;
}
}
}
$s[1]=$s[1]+0.5*$s[11];
$s[4]=$s[4]+0.4*$s[11];
$s[7]=$s[7]+$s[11];
$s[8]=$s[8]+$s[11];
$s[9]=$s[9]+0.2*$s[11];
for ($i=1;$i<=count($s);$i++){
echo $s1[$i]." балл ".$s[$i]."\n ";
	}
mysqli_close($link);
}
}
?>