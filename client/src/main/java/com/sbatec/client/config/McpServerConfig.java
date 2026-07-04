package com.sbatec.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbatec.client.services.IAClientService;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolRegistration;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import io.modelcontextprotocol.spec.McpSchema.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
public class McpServerConfig {

    private static final Logger log = LoggerFactory.getLogger(McpServerConfig.class);

    @Bean
    public HttpServletSseServerTransport mcpServerTransport(ObjectMapper objectMapper) {
        return new HttpServletSseServerTransport(objectMapper, "/mcp");
    }

    @Bean
    public MethodToolCallbackProvider methodToolCallbackProvider(IAClientService clientService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(clientService)
                .build();
    }

    @Bean
    public ToolCallback[] mcpServerTools(MethodToolCallbackProvider methodToolCallbackProvider) {
        return methodToolCallbackProvider.getToolCallbacks();
    }

    @Bean
    public McpSyncServer mcpServer(HttpServletSseServerTransport transport, ToolCallback[] mcpServerTools, ObjectMapper objectMapper) {

        List<SyncToolRegistration> mcpRegistrations = Arrays.stream(mcpServerTools)
                .map(callback -> {
                    Tool toolDefinition = new Tool(
                            callback.getToolDefinition().name(),
                            callback.getToolDefinition().description(),
                            callback.getToolDefinition().inputSchema()
                    );

                    return new SyncToolRegistration(toolDefinition, (Map<String, Object> arguments) -> {

                        String jsonArgs = "";
                        try {
                            jsonArgs = objectMapper.writeValueAsString(arguments);
                        } catch (Exception e) {
                            jsonArgs = arguments.toString();
                        }

                        log.info("[MCP SERVER] Appel de l'outil [{}] avec les paramètres : {}",
                                callback.getToolDefinition().name(), jsonArgs);

                        try {
                            // 🟢 CORRECTION : Utilisation de la String 'jsonArgs' attendue par l'API
                            String rawResult = callback.call(jsonArgs);
                            log.debug("[MCP SERVER] Outil [{}] exécuté avec succès.", callback.getToolDefinition().name());

                            TextContent textContent = new TextContent(rawResult);
                            return new CallToolResult(List.of(textContent), false);

                        } catch (Exception e) {
                            log.error("[MCP SERVER] Erreur lors de l'exécution de l'outil [{}]", callback.getToolDefinition().name(), e);
                            TextContent errorContent = new TextContent("Erreur interne : " + e.getMessage());
                            return new CallToolResult(List.of(errorContent), true);
                        }
                    });
                })
                .toList();

        return McpServer.sync(transport)
                .serverInfo("client-mcp-server", "1.0.0")
                .capabilities(ServerCapabilities.builder().tools(true).build())
                .tools(mcpRegistrations)
                .build();
    }
}