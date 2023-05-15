package org.example;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;

public class TagObject {
    private ExpandedNodeId nodeId;
    private DataValue value;

    public TagObject(ExpandedNodeId nodeId, DataValue value) {
        this.nodeId = nodeId;
        this.value = value;
    }

    public TagObject(ExpandedNodeId nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "TagObject{" +
                    "nodeId=" + nodeId.getIdentifier().toString() +
                    ", value=" + "null" + '}';
        } else {
            return "TagObject{" +
                    "nodeId=" + nodeId.getIdentifier().toString() +
                    ", value=" + value.getValue().getValue() + '}';
        }
    }

    public ExpandedNodeId getNodeId() {
        return nodeId;
    }

    public void setNodeId(ExpandedNodeId nodeId) {
        this.nodeId = nodeId;
    }

    public DataValue getValue() {
        return value;
    }

    public void setValue(DataValue value) {
        this.value = value;
    }
}
