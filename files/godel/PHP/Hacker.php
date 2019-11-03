<?php


class Hacker
{
    const BASEDIR = "/tmp/100hours/"; // trailing slash NEEDED

    private $name;
    private $time;
    private $file;

    public static function get() {
        return new self;
    }

    public static function getAll() {
        return array_diff(scandir(self::BASEDIR), array('..', '.'));
    }

    public function _print() {
        echo $this->name . "<br>";
        echo $this->file . "<br>";
        echo $this->time;
    }

    public function getByName($name) {
        $this->name = $name;
        $this->file = trim(self::BASEDIR . $this->name);

        if (file_exists($this->file)) {
            $this->time = $this->getTimeFromFile();
        } else {
            $this->setTime(0);
        }


        return $this;
    }

    public function setTime($time) {
        $this->time = (int)$time;
        $f = fopen($this->file, "w");
        fwrite($f, $this->time);
        fclose($f);
    }

    public function getName() {
        return $this->name;
    }

    public function getTime() {
        return $this->time;
    }

    public function getHours() {
        return (int)($this->time / (1000 * 60 * 60));
    }

    public function getMinutes() {
        return (int)(($this->time - $this->getHours() * 1000 * 60 * 60) / (1000 * 60));
    }

    public function getSeconds() {
        return (int)(($this->time - $this->getHours() * 1000 * 60 * 60 - $this->getMinutes() * 1000 * 60) / 1000);
    }

    private function getTimeFromFile() {
        return file_get_contents($this->file);
    }
}