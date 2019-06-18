<?php

require("phpMQTT.php");

$server = "mymind.mqtt.iot.bj.baidubce.com";     // change if necessary
$port = 1883;                     // change if necessary
$username = "mymind/mycry";                   // set your username
$password = "As6pXLkzzq8qzNk22rorqcuGC/4uz8bHThsTRJWxZug=";                   // set your password
$client_id = "PID_test0001"; // make sure this is unique for connecting to sever - you could use uniqid()

$mqtt = new phpMQTT($server, $port, $client_id);

if ($mqtt->connect(true, NULL, $username, $password)) {
	$mqtt->publish("mytest", "re", 0);
	$mqtt->close();
	echo "666";
} else {
    echo "Time out!\n";
}
header("Location:dongtailiebiao2.php");
