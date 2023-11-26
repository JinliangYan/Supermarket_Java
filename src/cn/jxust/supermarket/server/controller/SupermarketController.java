package cn.jxust.supermarket.server.controller;

import cn.jxust.supermarket.server.util.ControllerFactory;
import cn.jxust.supermarket.server.util.ServiceFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SupermarketController implements Controller{

    private final Map<String, Map<String, String>> requestMappings;
    private final Map<String, Controller> controllerInstances;

    private final ControllerFactory controllerFactory;

    public SupermarketController(ControllerFactory controllerFactory) {
        this.controllerFactory = controllerFactory;
        this.requestMappings = loadRequestMappings("C:\\Users\\Kokomi\\IdeaProjects\\supermarket\\src\\cn\\jxust\\supermarket\\server\\request_mapping.json");
        this.controllerInstances = new ConcurrentHashMap<>();
        creatControllerToMap(controllerFactory);
    }

    private void creatControllerToMap(ControllerFactory controllerFactory) {
        controllerInstances.put("CartController", controllerFactory.createCartController());
        controllerInstances.put("OrderController", controllerFactory.createOrderController());
        controllerInstances.put("PaymentController", controllerFactory.createPaymentController());
        controllerInstances.put("ProductController", controllerFactory.createProductController());
        controllerInstances.put("UserController", controllerFactory.createUserController());
    }

    private Map<String, Map<String, String>> loadRequestMappings(String configFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(configFile));
            return objectMapper.readValue(jsonData, new TypeReference<Map<String, Map<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error loading request mappings from file: " + configFile, e);
        }
    }

    public Object handleRequest(String requestType, Object requestData) {
        String[] requestParts = requestType.split(":");
        if (requestParts.length != 2) {
            throw new IllegalArgumentException("Invalid request format. Expected 'ControllerName:ActionName'.");
        }

        String controllerName = requestParts[0];
        String actionName = requestParts[1];
        Map<String, String> controllerMappings = requestMappings.get(controllerName);

        if (controllerMappings == null) {
            throw new IllegalArgumentException("Unknown controller: " + controllerName);
        }

        String controllerMethod = controllerMappings.get(actionName);
        if (controllerMethod == null) {
            throw new IllegalArgumentException("Unknown action: " + actionName);
        }

        try {
            // 使用反射调用控制器方法
            String[] parts = controllerMethod.split("\\.");
            String methodName = parts[parts.length - 1];

            Controller controller = controllerInstances.get(controllerName);

            Class<?> controllerClass = controller.getClass();
            Method method = Arrays.stream(controllerClass.getDeclaredMethods())
                    .filter(m -> m.getName().equals(methodName))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchMethodException("No such method: " + methodName));

            // 假设所有控制器方法都具有相同的参数类型
            return method.invoke(controller, requestData);

        } catch (Exception e) {
            throw new RuntimeException("Error executing controller method: " + controllerMethod, e);
        }
    }
}
