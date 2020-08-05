/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2020 DBeaver Corp and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudbeaver.service.sql;

import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.DBValueFormatting;
import org.jkiss.dbeaver.model.data.DBDAttributeBinding;
import org.jkiss.dbeaver.model.exec.DBExecUtils;
import org.jkiss.dbeaver.model.meta.Property;

/**
 * Web SQL query resultset.
 */
public class WebSQLQueryResultColumn {

    private static final Log log = Log.getLog(WebSQLQueryResultColumn.class);

    private final DBDAttributeBinding attrMeta;

    public WebSQLQueryResultColumn(DBDAttributeBinding attrMeta) {
        this.attrMeta = attrMeta;
    }

    @Property
    public Integer getPosition() {
        return attrMeta.getOrdinalPosition();
    }

    @Property
    public String getName() {
        return attrMeta.getName();
    }

    @Property
    public String getLabel() {
        return attrMeta.getLabel();
    }

    @Property
    public String getIcon() {
        return DBValueFormatting.getObjectImage(attrMeta).getLocation();
    }

    @Property
    public String getEntityName() {
        return attrMeta.getMetaAttribute().getEntityName();
    }

    @Property
    public String getDataKind() {
        return attrMeta.getDataKind().name();
    }

    @Property
    public String getTypeName() {
        return attrMeta.getTypeName();
    }

    @Property
    public String getFullTypeName() {
        return attrMeta.getFullTypeName();
    }

    @Property
    public long getMaxLength() {
        return attrMeta.getMaxLength();
    }

    @Property
    public Integer getScale() {
        return attrMeta.getScale();
    }

    @Property
    public Integer getPrecision() {
        return attrMeta.getPrecision();
    }

    @Property
    public boolean isReadOnly() {
        return DBExecUtils.isAttributeReadOnly(attrMeta);
    }

}
