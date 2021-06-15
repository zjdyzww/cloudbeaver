/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2021 DBeaver Corp and others
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

import io.cloudbeaver.model.WebPropertyInfo;
import io.cloudbeaver.model.session.WebSession;
import org.jkiss.code.NotNull;
import org.jkiss.dbeaver.Log;
import org.jkiss.dbeaver.model.exec.plan.DBCPlanNode;
import org.jkiss.dbeaver.runtime.properties.ObjectPropertyDescriptor;
import org.jkiss.dbeaver.runtime.properties.PropertyCollector;
import org.jkiss.utils.CommonUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * WebSQLExecutionPlanNode.
 */
public class WebSQLExecutionPlanNode {

    private static final Log log = Log.getLog(WebSQLExecutionPlanNode.class);

    private final WebSession webSession;
    private final DBCPlanNode node;

    public WebSQLExecutionPlanNode(WebSession webSession, DBCPlanNode node) {
        this.webSession = webSession;
        this.node = node;
    }

    public String getKind() {
        return node.getNodeKind().name();
    }

    public String getName() {
        return node.getNodeName();
    }

    public String getType() {
        return node.getNodeType();
    }

    public String getCondition() {
        return node.getNodeCondition();
    }

    public String getDescription() {
        return node.getNodeDescription();
    }

    @NotNull
    public WebPropertyInfo[] getProperties() {
        PropertyCollector propertyCollector = new PropertyCollector(node, false);
        propertyCollector.collectProperties();
        return Arrays.stream(propertyCollector.getProperties())
            .filter(p -> !(p instanceof ObjectPropertyDescriptor && ((ObjectPropertyDescriptor) p).isHidden()))
            .map(p -> new WebPropertyInfo(webSession, p, propertyCollector)).toArray(WebPropertyInfo[]::new);
    }

    public WebSQLExecutionPlanNode[] getNested() {
        Collection<? extends DBCPlanNode> nestedNodes = node.getNested();
        if (CommonUtils.isEmpty(nestedNodes)) {
            return null;
        }
        return nestedNodes
            .stream()
            .map(node1 -> new WebSQLExecutionPlanNode(webSession, node1))
            .toArray(WebSQLExecutionPlanNode[]::new);
    }

}
