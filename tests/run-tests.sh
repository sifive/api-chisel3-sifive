#!/usr/bin/env bash

set -euvo pipefail

# This script assumes that it is running from the root of the Wit workspace.

api_chisel3_sifive_path=./api-chisel3-sifive

wake --init .

# This is gross because we don't have a way of preventing Wake from
# automatically picking up .wake files that only make sense in specific contexts
# such as testing.
test_wake_files=$(find $api_chisel3_sifive_path -name '*.wake.template')
for file in $test_wake_files
do
  echo "ln -snf \"$(basename $file)\" \"${file%.*}\""
  ln -snf "$(basename $file)" "${file%.*}"
done

wake runAPIChisel3SiFiveTests Unit
