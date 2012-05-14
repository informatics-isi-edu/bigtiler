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
DIST=dist
CORE_PACKAGE=bigtiler-bin
AUTOMATION_PACKAGE=bigtiler-automation-bin
JDOCS_PACKAGE=bigtiler-docs

VERSION=2.1.0

tar_core: package_core
	cd $(DIST) && tar --exclude=.svn -czf $(CORE_PACKAGE)-$(VERSION).tar.gz \
	$(CORE_PACKAGE)

tar_automation: package_automation
	tar --exclude=.svn -czf $(DIST)/$(AUTOMATION_PACKAGE)-$(VERSION).tar.gz \
	$(DIST)/$(AUTOMATION_PACKAGE)

prepare: prepare_core prepare_automation

prepare_core:
	mkdir -p $(DIST)/$(CORE_PACKAGE)
	mkdir -p $(DIST)/$(CORE_PACKAGE)/lib
	mkdir -p $(DIST)/$(CORE_PACKAGE)/licenses
	echo "${VERSION}" > $(DIST)/$(CORE_PACKAGE)/VERSION

prepare_automation:
	mkdir -p $(DIST)/$(AUTOMATION_PACKAGE)
	mkdir -p $(DIST)/$(AUTOMATION_PACKAGE)/lib
	mkdir -p $(DIST)/$(AUTOMATION_PACKAGE)/licenses
	echo "${VERSION}" > $(DIST)/$(AUTOMATION_PACKAGE)/VERSION


build_core: prepare_core
	cd core && mvn install -DskipTests=true

build_automation: prepare_automation
	cd automation && mvn install -DskipTests=true

build: build_core build_automation

package_core: build_core
	cp -f scripts/common_min.sh $(DIST)/$(CORE_PACKAGE)
	cp -f scripts/cpappend.bat $(DIST)/$(CORE_PACKAGE)
	cp -f scripts/convert_image_annotation_to_zoomify.* $(DIST)/$(CORE_PACKAGE)
	cp -f scripts/convert_image_to_zoomify_tiles.* $(DIST)/$(CORE_PACKAGE)
	cp -f scripts/generate_thumbnail.* $(DIST)/$(CORE_PACKAGE)
	cp -f scripts/get_image_metadata.* $(DIST)/$(CORE_PACKAGE)
	cp -f core/target/*.jar $(DIST)/$(CORE_PACKAGE)/lib
	cp -f core/src/main/resources/log4j.xml $(DIST)/$(CORE_PACKAGE)
	cp -f licenses/* $(DIST)/$(CORE_PACKAGE)/licenses

package_automation: build_automation
	cp -f automation/target/*.jar $(DIST)/$(AUTOMATION_PACKAGE)/lib
	cp -f automation/src/main/resources/log4j.xml $(DIST)/$(AUTOMATION_PACKAGE)
	cp -f licenses/* $(DIST)/$(AUTOMATION_PACKAGE)/licenses

package: package_core package_automation

clean_core:
	rm -f $(DIST)/$(CORE_PACKAGE)-*.tar.gz
	rm -rf $(DIST)/$(CORE_PACKAGE)
	cd core && mvn clean

clean_automation:
	rm -f $(DIST)/$(AUTOMATION_PACKAGE)-*.tar.gz
	rm -rf $(DIST)/$(AUTOMATION_PACKAGE)
	cd automation && mvn clean

clean: clean_core clean_automation
