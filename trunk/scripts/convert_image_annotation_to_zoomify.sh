##################################################################
# Copyright 2011 University of Southern California
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
###################################################################
#!/bin/sh
DIR=$(dirname $0)
source "${DIR}/common_min.sh"

# Makes sure java is installed
check_prerequisites

CP=${DIR}
for i in ${DIR}/lib/*.jar; do
  CP=${CP}:${i}
done

if [ "${CP}x" != "x" ]; then
  CLASSPATH=${CLASSPATH}:${CP}
fi

java ${JAVA_OPTS} -cp "${CLASSPATH}" edu.isi.misd.image.gateway.conversion.ConvertImageAnnotationToZoomify "$@"

success=$?

exit ${success}

