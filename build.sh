#!/usr/bin/env bash
set -e

setup() {
    rm -rf venv/
    python3 -m venv venv
    source venv/bin/activate
    pip install --upgrade pip wheel setuptools
    pip install --editable .[test]
}

test() {
    pydocstyle timed_audio_player tests/
    pylint timed_audio_player/ tests/
    pytest --cov-branch --cov=timed_audio_player --cov-report html tests/
    coverage report --fail-under=100
}

package() {
    rm -rf dist/
    pip install --upgrade twine build
    python -m build
}

upload() {
    python -m twine upload --repository pypi dist/*
}

"$@"
