#!/usr/bin/env sh

yarn && yarn start &

PYTHON=$(command -v python3)
$PYTHON -m pip install -r requirements.txt

pytest -v

yarn stop
