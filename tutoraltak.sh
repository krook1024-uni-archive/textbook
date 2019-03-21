#!/usr/bin/env bash

echo "Tutoráltak száma: $(grep 'tutoráltam' tb-feladatok* | wc -l)"
echo "Engem tutoráltak: $(grep 'tutorált ' tb-feladatok* | wc -l)"
