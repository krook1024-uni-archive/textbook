<?php
session_start();
require_once "Hacker.php";
if (isset($_GET['time'])) {
    $hacker = Hacker::get()->getByName($_SESSION['hackername']);

    if ($hacker instanceof Hacker)
        $hacker->setTime($_GET['time']);
}

session_destroy();
header('Location: index.php');

