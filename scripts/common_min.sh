#!/bin/sh

# Checks whether the appropriate JRE version is installed
function check_jre
{
  local REQUIRED_JAVA=106
  local JRE_VERSION=1.6

  local JAVA_VERSION=`java -version 2>&1 | grep "java version" | awk '{ print substr($3, 2, length($3) - 2); }' | awk '{ print substr($1, 1, 3); }' | sed -e 's;\.;0;g'`
  local JAVA_INSTALLED=$?

  if [ ${JAVA_INSTALLED} -ne 0 ] || [ ${REQUIRED_JAVA} -gt ${JAVA_VERSION} ]; then
    echo "Java ${JRE_VERSION} must be installed and visible on the PATH."
    exit 1
  fi
}

# Minimum common functions
function check_prerequisites
{
  check_jre
}

