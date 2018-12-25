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
package com.tcdng.jacklyn.common.business;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.common.annotation.Format;
import com.tcdng.jacklyn.common.annotation.Managed;
import com.tcdng.jacklyn.common.constants.CommonModuleNameConstants;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.constants.CommonModuleErrorConstants;
import com.tcdng.jacklyn.common.constants.JacklynModuleStaticSettings;
import com.tcdng.jacklyn.common.constants.JacklynPropertyConstants;
import com.tcdng.jacklyn.common.utils.JacklynUtils;
import com.tcdng.jacklyn.shared.archiving.ArchivingFieldType;
import com.tcdng.jacklyn.shared.security.PrivilegeCategoryConstants;
import com.tcdng.jacklyn.shared.xml.config.module.ArchiveConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ArchivesConfig;
import com.tcdng.jacklyn.shared.xml.config.module.AuditConfig;
import com.tcdng.jacklyn.shared.xml.config.module.AuditsConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ShortcutTileConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ShortcutTilesConfig;
import com.tcdng.jacklyn.shared.xml.config.module.FieldConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ManagedConfig;
import com.tcdng.jacklyn.shared.xml.config.module.MenuConfig;
import com.tcdng.jacklyn.shared.xml.config.module.MenuItemConfig;
import com.tcdng.jacklyn.shared.xml.config.module.MenusConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.module.PrivilegeConfig;
import com.tcdng.jacklyn.shared.xml.config.module.PrivilegeGroupConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ReportConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ReportsConfig;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyStaticSettings;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.application.AbstractBootService;
import com.tcdng.unify.core.application.BootInstallationInfo;
import com.tcdng.unify.core.application.StartupShutdownHook;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.logging.EventType;
import com.tcdng.unify.core.report.ReportServer;
import com.tcdng.unify.core.stream.XMLObjectStreamer;
import com.tcdng.unify.core.util.AnnotationUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Jacklyn boot business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component(CommonModuleNameConstants.JACKLYNBOOTSERVICE)
public class JacklynBootServiceImpl extends AbstractBootService<ModuleConfig> {

    @Configurable(ApplicationComponents.APPLICATION_XMLOBJECTSTREAMER)
    private XMLObjectStreamer xmlObjectStreamer;

    @Override
    protected List<StartupShutdownHook> getStartupShutdownHooks() throws UnifyException {
        List<StartupShutdownHook> list = new ArrayList<StartupShutdownHook>();
        for (UnifyStaticSettings unifyStaticSettings : getStaticSettings()) {
            if (unifyStaticSettings instanceof JacklynModuleStaticSettings) {
                JacklynModuleStaticSettings jacklynModuleStaticSettings = (JacklynModuleStaticSettings) unifyStaticSettings;
                if (!StringUtils.isBlank(jacklynModuleStaticSettings.getModuleComponent())) {
                    String componentName = jacklynModuleStaticSettings.getModuleComponent();
                    Class<? extends UnifyComponent> type = getComponentType(componentName);
                    if (StartupShutdownHook.class.isAssignableFrom(type)) {
                        logDebug("Identified startup-shutdown hook [{0}]...", componentName);
                        list.add((StartupShutdownHook) getComponent(componentName));
                    }
                }
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected BootInstallationInfo<ModuleConfig> prepareBootInstallation() throws UnifyException {
        List<ModuleConfig> list = new ArrayList<ModuleConfig>();
        List<String> installerList = new ArrayList<String>();
        List<String> moduleXmlList = new ArrayList<String>();
        for (UnifyStaticSettings unifyStaticSettings : getStaticSettings()) {
            if (unifyStaticSettings instanceof JacklynModuleStaticSettings) {
                JacklynModuleStaticSettings jacklynModuleStaticSettings = (JacklynModuleStaticSettings) unifyStaticSettings;
                String componentName = jacklynModuleStaticSettings.getModuleComponent();
                if (!StringUtils.isBlank(componentName)) {
                    logDebug("Identified feature installer [{0}]...", componentName);
                    installerList.add(componentName);
                }

                if (!StringUtils.isBlank(jacklynModuleStaticSettings.getModuleConfig())) {
                    moduleXmlList.add(jacklynModuleStaticSettings.getModuleConfig());
                }
            }
        }

        List<String> configXmlList = DataUtils.convert(ArrayList.class, String.class,
                getContainerSetting(Object.class, JacklynPropertyConstants.MODULE_CONFIGURATION), null);
        if (configXmlList != null) {
            moduleXmlList.addAll(configXmlList);
        }

        for (String configFile : moduleXmlList) {
            logDebug("Loading feature definitions from [{0}]...", configFile);
            InputStream inputStream = null;
            try {
                inputStream = IOUtils.openFileResourceInputStream(configFile,
                        getUnifyComponentContext().getWorkingPath());
                ModuleConfig mc = xmlObjectStreamer.unmarshal(ModuleConfig.class, inputStream, null);
                list.add(mc);
                mc.getPrivileges().toMap();
            } finally {
                IOUtils.close(inputStream);
            }
        }

        // Add managed records configuration
        addManagedRecordConfigurations(list);

        // Add implicit privilege configurations
        addImplicitPrivilegeConfigurations(list);
        return new BootInstallationInfo<ModuleConfig>(installerList, list);
    }

    @Override
    protected void onStartup() throws UnifyException {
        wireApplicationProvider(ReportProvider.class, JacklynApplicationAttributeConstants.COMMON_REPORT_PROVIDER,
                JacklynPropertyConstants.COMMON_REPORT_PROVIDER);

        wireApplicationProvider(ReportServer.class, JacklynApplicationAttributeConstants.COMMON_REPORT_SERVER,
                JacklynPropertyConstants.COMMON_REPORT_SERVER);

        wireApplicationProvider(RemoteCallSystemAssetProvider.class,
                JacklynApplicationAttributeConstants.RC_SYSTEMASSET_PROVIDER,
                JacklynPropertyConstants.RC_SYSTEMASSET_PROVIDER);
    }

    @Override
    protected void onShutdown() throws UnifyException {

    }

    @SuppressWarnings("unchecked")
    private <T extends UnifyComponent> void wireApplicationProvider(Class<T> type, String contextID, String configID)
            throws UnifyException {
        // Check configuration first
        String providerName = getContainerSetting(String.class, configID, null);
        if (StringUtils.isBlank(providerName)) {
            // Auto-detect and select anyone if no configuration
            List<UnifyComponentConfig> providerConfigList = getComponentConfigs(type);
            if (!providerConfigList.isEmpty()) {
                providerName = providerConfigList.get(0).getName();
                logInfo("Wiring provider with context ID [{0}] using [{1}] via auto-detection...", contextID,
                        providerName);
            }
        } else {
            logInfo("Wiring provider with context ID [{0}] using [{1}] via configuration [{2}]...", contextID,
                    providerName, configID);
        }

        if (!StringUtils.isBlank(providerName)) {
            setApplicationAttribute(contextID, (T) getComponent(providerName));
        } else {
            logWarn("Could not find provider component with context ID [{0}] and config ID [{1}].", contextID,
                    configID);
        }
    }

    private void addImplicitPrivilegeConfigurations(List<ModuleConfig> list) throws UnifyException {
        for (ModuleConfig mc : list) {
            // Shortcut privileges
            ShortcutTilesConfig dtc = mc.getShortcutTiles();
            if (dtc != null) {
                PrivilegeGroupConfig pgc = getPrivilegeGroup(mc, PrivilegeCategoryConstants.SHORTCUT);
                for (ShortcutTileConfig dtci : dtc.getShortcutTileList()) {
                    PrivilegeConfig pc = new PrivilegeConfig(dtci.getName(), dtci.getDescription());
                    pgc.addPrivilegeConfig(pc);
                }
            }

            // Report privileges
            ReportsConfig rc = mc.getReports();
            if (rc != null) {
                PrivilegeGroupConfig pgc = getPrivilegeGroup(mc, PrivilegeCategoryConstants.REPORTABLE);
                for (ReportConfig rci : rc.getReportList()) {
                    PrivilegeConfig pc = new PrivilegeConfig(rci.getName(), rci.getDescription());
                    pgc.addPrivilegeConfig(pc);
                }
            }

            // Menu privileges
            // if (SystemModuleNameConstants.SYSTEM_MODULE.equals(mc.getName())) {
            // PrivilegeGroupConfig pgc
            // = getPrivilegeGroup(mc, PrivilegeCategoryConstants.APPLICATIONUI);
            // PrivilegeConfig pc = new PrivilegeConfig(getApplicationName(),
            // getApplicationMessage("application.privilege", getApplicationName()));
            // pgc.addPrivilegeConfig(pc);
            // }

            MenusConfig mnc = mc.getMenus();
            if (mnc != null) {
                PrivilegeGroupConfig pgc = getPrivilegeGroup(mc, PrivilegeCategoryConstants.APPLICATIONUI);
                for (MenuConfig mnci : mnc.getMenuList()) {
                    PrivilegeConfig pc = new PrivilegeConfig(mnci.getName(), mnci.getDescription());
                    pgc.addPrivilegeConfig(pc);
                    for (MenuItemConfig mic : mnci.getMenuItemList()) {
                        pc = new PrivilegeConfig(mic.getName(), mic.getDescription());
                        pgc.addPrivilegeConfig(pc);
                    }
                }
            }
        }
    }

    private void addManagedRecordConfigurations(List<ModuleConfig> list) throws UnifyException {
        Map<String, ModuleConfig> moduleConfigs = new HashMap<String, ModuleConfig>();
        for (ModuleConfig mc : list) {
            moduleConfigs.put(mc.getName(), mc);
        }

        // Managed record types
        for (Class<? extends Entity> entityClass : getAnnotatedClasses(Entity.class, Managed.class)) {
            Managed ma = entityClass.getAnnotation(Managed.class);
            ModuleConfig mc = moduleConfigs.get(ma.module());
            if (mc == null) {
                throw new UnifyException(CommonModuleErrorConstants.UNKNOWN_MODULE_REFERENCED_BY_RECORD, ma.module(),
                        entityClass);
            }

            // Create managed configuration
            ManagedConfig managedConfig = new ManagedConfig();
            managedConfig.setType(entityClass.getName());

            List<ManagedField> managedFieldList = new ArrayList<ManagedField>();
            for (Field f : ReflectUtils.getAnnotatedFields(entityClass, Column.class)) {
                managedFieldList.add(new ManagedField(f, false));
            }

            for (Field f : ReflectUtils.getAnnotatedFields(entityClass, ListOnly.class)) {
                managedFieldList.add(new ManagedField(f, true));
            }

            Set<String> excludArch = new HashSet<String>(Arrays.asList(ma.excludeOnArchive()));
            Set<String> excludAud = new HashSet<String>(Arrays.asList(ma.excludeOnAudit()));
            Set<String> excludRpt = new HashSet<String>(Arrays.asList(ma.excludeOnReport()));
            for (ManagedField managedField : managedFieldList) {
                FieldConfig fc = new FieldConfig();
                Field field = managedField.getField();
                String fieldName = field.getName();

                fc.setType(DataUtils.getWrapperClassName(field.getType()));
                ColumnType ct = DataUtils.getColumnType(field);
                if (ct != null) {
                    fc.setArchFieldType(ArchivingFieldType.fromCode(ct.code()));
                    if (!managedField.isListOnly()) {
                        fc.setArchivable(
                                ma.archivable() && fc.getArchFieldType() != null && !excludArch.contains(fieldName));
                    }
                }

                String description = null;
                Format fa = field.getAnnotation(Format.class);
                if (fa != null) {
                    description = AnnotationUtils.getAnnotationString(fa.description());
                    if (description != null) {
                        description = resolveApplicationMessage(description);
                    }

                    String formatter = AnnotationUtils.getAnnotationString(fa.formatter());
                    fc.setFormatter(formatter);
                    fc.sethAlign(fa.halign().name());
                    fc.setList(fa.list());
                    fc.setMask(fa.mask());
                    fc.setWidth(fa.widthRatio());
                }

                if (description == null) {
                    description = NameUtils.describeName(fieldName);
                }

                fc.setName(fieldName);
                fc.setDescription(description);
                fc.setAuditable(ma.auditable() && !managedField.isListOnly() && !excludAud.contains(fieldName));
                fc.setReportable(ma.reportable() && !excludRpt.contains(fieldName));
                managedConfig.addFieldConfig(fc);
            }

            String type = entityClass.getName();
            String title = JacklynUtils.generateManagedRecordTitle(entityClass);
            title = resolveApplicationMessage(title);
            String titleLowCase = title.toLowerCase();
            String namePrefix = ma.module().toLowerCase() + '-' + StringUtils.squeeze(title);

            // Archivable configuration
            if (ma.archivable()) {
                ArchivesConfig archivesConfig = mc.getArchives();
                if (archivesConfig == null) {
                    mc.setArchives(archivesConfig = new ArchivesConfig());
                }

                List<ArchiveConfig> archiveList = archivesConfig.getArchiveList();
                if (archiveList == null) {
                    archivesConfig.setArchiveList(archiveList = new ArrayList<ArchiveConfig>());
                }

                String name = namePrefix + "-arch";
                ArchiveConfig ac = new ArchiveConfig();
                ac.setName(name);
                ac.setDescription(title);
                ac.setArchivable(type);
                archiveList.add(ac);
            }

            // Auditable configuration
            if (ma.auditable()) {
                AuditsConfig auditsConfig = mc.getAudits();
                if (auditsConfig == null) {
                    mc.setAudits(auditsConfig = new AuditsConfig());
                }

                List<AuditConfig> auditConfigList = auditsConfig.getAuditList();
                if (auditConfigList == null) {
                    auditsConfig.setAuditList(auditConfigList = new ArrayList<AuditConfig>());
                }

                auditConfigList.add(createAuditConfig(namePrefix, titleLowCase, type, EventType.SEARCH, false));
                auditConfigList.add(createAuditConfig(namePrefix, titleLowCase, type, EventType.CREATE, true));
                auditConfigList.add(createAuditConfig(namePrefix, titleLowCase, type, EventType.VIEW, false));
                auditConfigList.add(createAuditConfig(namePrefix, titleLowCase, type, EventType.UPDATE, true));
                auditConfigList.add(createAuditConfig(namePrefix, titleLowCase, type, EventType.DELETE, true));
            }

            // Reportable configuration
            if (ma.reportable()) {
                ReportsConfig reportsConfig = mc.getReports();
                if (reportsConfig == null) {
                    mc.setReports(reportsConfig = new ReportsConfig());
                }

                List<ReportConfig> reportConfigList = reportsConfig.getReportList();
                if (reportConfigList == null) {
                    reportsConfig.setReportList(reportConfigList = new ArrayList<ReportConfig>());
                }

                String name = JacklynUtils.generateManagedRecordReportableName(entityClass, title);
                String description = resolveApplicationMessage("$m{module.managedreport}", title);
                ReportConfig rc = new ReportConfig();
                rc.setName(name);
                rc.setDescription(description);
                rc.setDynamic(true);
                rc.setParameters(null);
                rc.setProcessor(null);
                rc.setReportable(type);
                rc.setTemplate(null);
                rc.setTitle(title);
                rc.setManaged(true);
                reportConfigList.add(rc);
            }

            // Add to module configuration
            mc.add(managedConfig);
        }
    }

    private AuditConfig createAuditConfig(String namePrefix, String titleLowCase, String type, EventType eventType, boolean active)
            throws UnifyException {
        AuditConfig ac = new AuditConfig();
        String action = eventType.name().toLowerCase();
        ac.setAction(eventType);
        ac.setAuditable(type);
        ac.setName(namePrefix + '-' + action);
        ac.setDescription(resolveApplicationMessage("$m{module.managedaudit." + action + "}", titleLowCase));
        ac.setActive(active);
        return ac;
    }

    private PrivilegeGroupConfig getPrivilegeGroup(ModuleConfig mc, String category) throws UnifyException {
        PrivilegeGroupConfig pgc = mc.getPrivileges().getPrivilegeGroupConfig(category);
        if (pgc == null) {
            pgc = new PrivilegeGroupConfig(category);
            mc.getPrivileges().addPrivilegeGroup(pgc);
        }
        return pgc;
    }

    private class ManagedField {

        private Field field;

        private boolean listOnly;

        public ManagedField(Field field, boolean listOnly) {
            this.field = field;
            this.listOnly = listOnly;
        }

        public Field getField() {
            return field;
        }

        public boolean isListOnly() {
            return listOnly;
        }
    }
}
