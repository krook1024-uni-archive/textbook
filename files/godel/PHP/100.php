<?php
error_reporting(E_ALL);
session_start();
require_once "Hacker.php";

if ( ! (isset($_SESSION["hackerin"]) && $_SESSION["hackerin"] == true)) {
    header("location: index.php");
    exit;
}

$hacker = Hacker::get()->getByName($_SESSION['hackername']);

if ( ! empty($_GET['time'])) {
    $hacker->setTime($_GET['time']);
}

?>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        p {
            margin-top: 0px;
            text-align: center;
            font-size: 55px;
            font-variant: small-caps;
        }

        img {
            display: block;
            margin-left: auto;
            margin-right: auto;
        }


        .hacker {
            display: block;
        }
    </style>
</head>
<body>

<p id="ido">Streameljünk 100 óra DEAC játékot!</p>
<img src="http://deac.hu/upload/5b376e152b0b5.png" width="300">
<p id="hanyan" class="smallcap">Hány DEAC-Hackers streamel?</p>

<div align="center">
    Hajrá,
    <?= $hacker->getName(); ?>!
</div>

<div align="center">
    <button onclick="refresh100()">FRISSÍT</button>
    <button onclick="stop100()">LEÁLLÍT</button>
</div>

<div align="center">

    <?php
    $hackers = Hacker::get()->getAll();

    foreach ($hackers as $h) {
        $tmp = Hacker::get()->getByName($h);
        $hours = $tmp->getHours();
        $minutes = $tmp->getMinutes();
        $seconds = $tmp->getSeconds();

        echo "<span class='hacker' data-time='" . $tmp->getTime() . "'>";
        echo "Hacker: " . $h . ", idő: " . $hours . " óra " . $minutes . " perc " . $seconds . " mp";
        echo "</span>";
    }
    ?>

</div>
<script>
    function sortThings() {
        Array.from(document.querySelectorAll("span[data-time]"))
            .sort(({dataset: {time: a}}, {dataset: {time: b}}) => parseInt(a) < parseInt(b))
            .forEach((item) => item.parentNode.appendChild(item));
    }

    var limit = 1000 * 60 * 60 * 100;
    var refreshlimit = 1000 * 60 * 5;
    var nofhackers = 1;
    var time = <?php echo is_numeric($hacker->getTime()) ? $hacker->getTime() : 0; ?>;
    var localtime = 0;

    setInterval(function () {
        sortThings();

        time = time + nofhackers * 1000;
        localtime = localtime + 1000;

        var hours = Math.floor(time / (1000 * 60 * 60));
        var minutes = Math.floor((time - hours * 1000 * 60 * 60) / (1000 * 60));
        var seconds = Math.floor((time - hours * 1000 * 60 * 60 - minutes * 1000 * 60) / 1000);

        document.getElementById("ido").innerHTML = hours + " óra "
            + minutes + " perc " + seconds + " mp ";
        if (nofhackers === 1)
            document.getElementById("hanyan").innerHTML = nofhackers + " hacker streamel";
        else
            document.getElementById("hanyan").innerHTML = nofhackers + " hacker streamel párhuzamosan";

        if (time >= limit) {
            document.getElementById("ido").innerHTML = "A 100 óra stream teljesítve!";
        }

        if (localtime >= refreshlimit) {
            localtime = 0;
            refresh100();
        }

    }, 1000);

    function stop100() {
        window.location.href = "stop.php?time=" + time;
    }

    function refresh100() {
        window.location.href = "100.php?time=" + time;
    }

</script>

</body>
</html>
