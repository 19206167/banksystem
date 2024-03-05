#!/usr/bin/env bash

docker pull owasp/zap2docker-stable
docker run -i owasp/zap2docker-stable zap-baseline.py -t "http://13.213.36.80/" -l PASS > zap_baseline_report.html

echo $? > /dev/null
