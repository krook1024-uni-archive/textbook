<?php
    session_start();
    require_once "Hacker.php";

    if (isset($_POST['name'])) {
        $_SESSION['hackername'] = $_POST['name'];
        $hacker = Hacker::get()->getByName($_SESSION['hackername']);
        $_SESSION['hackertime'] = $hacker->getTime();
        $_SESSION['hackerin'] = TRUE;
        header('Location: 100.php');
    }
?>

<style>
    body {
        display: flex;
        align-items: center;
        justify-content: center;
        font-family: sans-serif;
        background: #f5f5f5;
    }
    form {
        display: flex;
        flex-flow: column wrap;

        background: rgb(2,0,36);
        background: linear-gradient(-45deg, rgba(9,9,121,1) 35%, rgba(0,212,255,1) 100%);
        color: white;

        padding: 2em;
        border-radius: 4px;
    }
    input {
        margin: .5em 0;
        background: #f5f5f5;
        color: #333;
        border: 0 none;
        padding: .5em;
        box-sizing: border-box;
    }
</style>

<form method="POST">
    <label for="name">Név</label>
    <input type="text" name="name" id="name" placeholder="Név" />
    <input type="submit" value="Bejelentkezés" />
</form>
