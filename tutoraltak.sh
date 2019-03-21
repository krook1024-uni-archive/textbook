#!/usr/bin/env bash

echo "Tutoráltak száma: $(grep 'tutoráltam' tb-feladatok* | wc -l)"
