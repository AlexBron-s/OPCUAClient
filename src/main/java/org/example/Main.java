package org.example;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class Main {

    public static void main(String[] args) throws Exception {
        OpcUaClient client = OpcUaClient.create(
                "opc.tcp://desktop-8ad18fo:62640/IntegrationObjects/ServerSimulator",
                endpoints ->
                        endpoints.stream()
                                .filter(e -> e.getSecurityPolicyUri().equals(SecurityPolicy.None.getUri()))
                                .findFirst(),
                OpcUaClientConfigBuilder::build
        );
        client.connect().get();

        // Адрес пространства с тегами
        NodeId nodeId  = new NodeId(2, "Realtimedata");

        // 1. Считать список тегов
        BrowseDescription browse = new BrowseDescription(
                nodeId,
                BrowseDirection.Forward,
                Identifiers.References,
                true,
                uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
                uint(BrowseResultMask.All.getValue())
        );

        BrowseResult browseResult = client.browse(browse).get();

        ReferenceDescription[] refs = browseResult.getReferences();
        ArrayList<TagObject> tags = new ArrayList<>();
        List<NodeId> nodeIds = new ArrayList<>();
        for (ReferenceDescription referenceDescription : refs) {
            tags.add(new TagObject(referenceDescription.getNodeId()));
            nodeIds.add(new NodeId(referenceDescription.getNodeId().getNamespaceIndex(),
                    (String) referenceDescription.getNodeId().getIdentifier()));
        }

        // 2. Сохранить в текстовй файл
        TagRepositoryFileImpl.write(tags);

        // 3. Считать значение тегов
        List<DataValue> values =
                client.readValues(0, TimestampsToReturn.Both, nodeIds)
                        .get();

        tags.clear();
        for (int i = 0; i < nodeIds.size(); i++) {
            tags.add(new TagObject(nodeIds.get(i).expanded(), values.get(i)));
        }

        // 4. Сохранить значение тегов в файл
        TagRepositoryFileImpl.write(tags);

        Subscriptions subscriptions = new Subscriptions(tags);
        subscriptions.run(new NodeId(2, "Tag14"),
                client,new CompletableFuture<>());

        int i = 0;
        client.disconnect();
    }

}