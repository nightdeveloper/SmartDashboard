package com.github.nightdeveloper.smartdashboard.controller;

import com.github.nightdeveloper.mdns_explorer.dns.Domain;
import com.github.nightdeveloper.mdns_explorer.sd.Instance;
import com.github.nightdeveloper.mdns_explorer.sd.Query;
import com.github.nightdeveloper.mdns_explorer.sd.Service;
import com.github.nightdeveloper.smartdashboard.common.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class DiscoveryController {

    final static Logger logger = LogManager.getLogger(DiscoveryController.class);

    private static List<InetAddress> getLocalAddresses() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        List<InetAddress> result = new ArrayList<>();
        while (networkInterfaces.hasMoreElements()) {
            for (InterfaceAddress interfaceAddress : networkInterfaces.nextElement().getInterfaceAddresses()) {
                result.add(interfaceAddress.getAddress());
            }
        }

        return result;
    }

    private static void logToStream(HttpServletResponse response, String str) throws IOException {
        logger.info(str);
        response.getOutputStream().print(str + "<br/>");
        response.getOutputStream().flush();
    }

    @RequestMapping(value = Constants.ENDPOINT_DISCOVERY, method = RequestMethod.GET)
    @ResponseBody
    public void discovery(Principal principal, HttpServletResponse response) throws Exception {

        logger.info("started discovery " + principal.getName());

        response.setBufferSize(0);
        response.setContentType("text/event-stream");

        String hostname = InetAddress.getLocalHost().getHostName();

        logToStream(response, "Using hostname " + hostname);

        List<InetAddress> addresses = getLocalAddresses();

        for (InetAddress address : addresses) {

            logToStream(response, "querying interface " + address.getHostAddress());

            Service discoveryService = Service.fromName(Service.SERVICE_QUERY);
            Query serviceQuery = Query.createFor(discoveryService, Domain.LOCAL);
            Set<Instance> serviceInstances = serviceQuery.runOnce(address);
            logToStream(response, "got count " + serviceInstances.size());

            List<String> serviceNames = serviceInstances.stream().map(Instance::getFullName)
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList());


            for (String serviceName : serviceNames) {
                logToStream(response, "Discovered service " + serviceName + ":");

                Query instanceQuery = Query.createFor(Service.fromName(serviceName), Domain.LOCAL);
                Set<Instance> instances = instanceQuery.runOnce(address);

                instances.forEach(instance -> {
                    try {
                        logToStream(response, " - ["

                                + instance.getAddresses().stream()
                                .map(inetAddress ->
                                        inetAddress.getHostName().equals(inetAddress.getHostAddress()) ?
                                                inetAddress.getHostAddress() :
                                                inetAddress.getHostName() + " " + inetAddress.getHostAddress())
                                .collect(Collectors.joining(", "))

                                + "] port " + instance.getPort()

                                + (instance.getAttributes().size() > 0 ?
                                " attributes: " + instance.getAttributes().keySet().stream()
                                        .map(s -> s + " = " + instance.getAttributes().get(s))
                                        .collect(Collectors.joining(", "))
                                : "")
                        );
                    } catch (IOException e) {
                        logger.error("instance exception", e);
                    }
                });

                logToStream(response, "");
            }
        }
    }
}
