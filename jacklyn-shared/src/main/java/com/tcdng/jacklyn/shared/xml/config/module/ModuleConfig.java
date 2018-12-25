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
package com.tcdng.jacklyn.shared.xml.config.module;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;
import com.tcdng.unify.core.application.FeatureDefinition;

/**
 * Managed configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
@XmlRootElement(name = "module")
public class ModuleConfig extends BaseConfig implements FeatureDefinition {

    private Map<String, ManagedConfig> managedConfigs;

    private PrivilegesConfig privileges;

    private ArchivesConfig archives;

    private AuditsConfig audits;

    private ShortcutTilesConfig shortcutTiles;

    private InputControlsConfig inputControls;

    private MenusConfig menus;

    private NotificationTemplatesConfig notificationTemplates;

    private ReportsConfig reports;

    private SysParamsConfig sysParams;

    private boolean extension;

    private boolean deactivatable;

    public ModuleConfig() {
        this.privileges = new PrivilegesConfig();
        this.managedConfigs = new HashMap<String, ManagedConfig>();
    }

    public boolean isDeactivatable() {
        return deactivatable;
    }

    @XmlAttribute
    public void setDeactivatable(boolean deactivatable) {
        this.deactivatable = deactivatable;
    }

    public boolean isExtension() {
        return extension;
    }

    @XmlAttribute
    public void setExtension(boolean extension) {
        this.extension = extension;
    }

    public PrivilegesConfig getPrivileges() {
        return privileges;
    }

    @XmlElement(name = "privileges")
    public void setPrivileges(PrivilegesConfig privileges) {
        this.privileges = privileges;
    }

    public ArchivesConfig getArchives() {
        return archives;
    }

    @XmlElement(name = "archives")
    public void setArchives(ArchivesConfig archives) {
        this.archives = archives;
    }

    public AuditsConfig getAudits() {
        return audits;
    }

    @XmlElement(name = "audits")
    public void setAudits(AuditsConfig audits) {
        this.audits = audits;
    }

    public ShortcutTilesConfig getShortcutTiles() {
        return shortcutTiles;
    }

    @XmlElement(name = "shortcut-tiles")
    public void setShortcutTiles(ShortcutTilesConfig shortcutTiles) {
        this.shortcutTiles = shortcutTiles;
    }

    public InputControlsConfig getInputControls() {
        return inputControls;
    }

    @XmlElement(name = "input-controls")
    public void setInputControls(InputControlsConfig inputControls) {
        this.inputControls = inputControls;
    }

    public MenusConfig getMenus() {
        return menus;
    }

    @XmlElement(name = "menus")
    public void setMenus(MenusConfig menus) {
        this.menus = menus;
    }

    public NotificationTemplatesConfig getNotificationTemplates() {
        return notificationTemplates;
    }

    @XmlElement(name = "notification-templates")
    public void setNotificationTemplates(NotificationTemplatesConfig notificationTemplates) {
        this.notificationTemplates = notificationTemplates;
    }

    public ReportsConfig getReports() {
        return reports;
    }

    @XmlElement(name = "reports")
    public void setReports(ReportsConfig reports) {
        this.reports = reports;
    }

    public SysParamsConfig getSysParams() {
        return sysParams;
    }

    @XmlElement(name = "sys-parameters")
    public void setSysParams(SysParamsConfig sysParams) {
        this.sysParams = sysParams;
    }

    public void add(ManagedConfig managedConfig) {
        this.managedConfigs.put(managedConfig.getType(), managedConfig);
    }

    public ManagedConfig getManagedConfig(String type) {
        return this.managedConfigs.get(type);
    }
}
