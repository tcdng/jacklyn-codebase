/*
 * Copyright 2018 The Code Department
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.jacklyn.archiving;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.tcdng.jacklyn.archiving.business.FileArchiveNameGenerator;
import com.tcdng.jacklyn.common.AbstractJacklynTest;
import com.tcdng.jacklyn.shared.archiving.FileArchiveType;

/**
 * Default file archive name generator tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileArchiveNameGeneratorTest extends AbstractJacklynTest {

	@Test
	public void testGenerateFileArchiveName() throws Exception {
		FileArchiveNameGenerator fileArchiveNameGenerator
				= (FileArchiveNameGenerator) this.getComponent("default-filearchivenamegenerator");
		Date workingDt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String expectedFilename = "lobchequeimagecfg" + sdf.format(workingDt) + "0001.arch";
		String generatedFilename = fileArchiveNameGenerator.generateFileArchiveName(
				FileArchiveType.LOB_FILE_ARCHIVE, "ChequeImageCfg", workingDt);
		assertEquals(expectedFilename, generatedFilename);
	}

	@Override
	protected void onSetup() throws Exception {

	}

	@Override
	protected void onTearDown() throws Exception {

	}
}
